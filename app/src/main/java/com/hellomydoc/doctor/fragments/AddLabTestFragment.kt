package com.hellomydoc.doctor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.hellomydoc.doctor.*
import com.hellomydoc.doctor.activities.AddPrescriptionActivity
import com.hellomydoc.doctor.databinding.FragmentAddLabTestBinding
import java.net.URL

class AddLabTestFragment : Fragment() {
    private var labTests: MutableList<String> = mutableListOf()
    private lateinit var binding: FragmentAddLabTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddLabTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(AddPrescriptionActivity::class.java.simpleName){
            if(it?.key==Constants.MESSAGE_PLEASE_DESTROY){
                finalizeAndReturn()
            }
        }

        updateLabTestsRendering()

        binding.ratvTests.apply {
            itemResolver = {list,pos->
                (list?.getOrNull(pos) as? String)?:""
            }
            urlComposer = {
                URL("${Metar[Constants.BASE_URL]}/searchLabTests/$it")
            }
            processor = {
                it.toMutableList()
            }
        }

        binding.btSave.setOnClickListener {
            finalizeAndReturn()
        }

        binding.btAddNext.setOnClickListener {
            finalize()
        }
    }

    private fun finalizeAndReturn() {
        val input = binding.ratvTests.text.toString()
        if(input.isEmpty || finalize()){
            returnBack()
        }
    }

    private fun returnBack() {
        publish(Message(Constants.MESSAGE_LAB_TESTS,labTests))
        publish(Message(Constants.MESSAGE_FINISHED))
    }

    private fun finalize(): Boolean {
        val input = binding.ratvTests.text.toString()
        if(input.isEmpty){
            toastShort(R.string.please_select_lab_test.string)
            return false
        }
        labTests.add(input)
        binding.ratvTests.setText("")
        updateLabTestsRendering()
        return true
    }

    private fun updateLabTestsRendering() {
        binding.alvTests.composeRender(labTests){pos,data->
            val bold400 = Font(R.font.roboto_bold, FontWeight.W400)
            Card(
                modifier = Modifier.padding(4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 2.dp,
                border = BorderStroke(1.dp, Color(250,250,250))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(data?.toString()?.uppercase()?:"",style = TextStyle(
                        fontFamily = FontFamily(bold400),
                        fontSize = 20.sp,
                        color = colorResource(R.color.blue)
                    )
                    )
                    IconButton(onClick = {
                        labTests.removeAt(pos)
                        updateLabTestsRendering()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_forever_24),
                            tint = Color.Red,
                            contentDescription = null // decorative element
                        )
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        cancelSubscription()
        publish(Message(Constants.MESSAGE_DESTROYED))
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(lt: MutableList<String>) = AddLabTestFragment().apply {
            labTests = lt
        }
    }
}