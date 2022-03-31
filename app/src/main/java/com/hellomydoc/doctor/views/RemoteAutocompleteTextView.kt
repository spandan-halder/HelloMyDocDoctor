package com.hellomydoc.doctor.views

import android.R
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RemoteAutocompleteTextView : androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    private var data: MutableList<Any?>? = null

    var onValueSelected: ((Any?)->Unit)? = null
    var itemResolver: ((MutableList<Any?>?,Int)->String)? = null
    var urlComposer: ((String?)-> URL)? = null
    var processor: ((ArrayList<String>)->MutableList<Any?>?)? = null

    private fun onValueSelected(value: Any?) {
        if (onValueSelected != null) {
            onValueSelected?.invoke(value)
        }
    }

    ///////////////////////////////
    constructor(context: Context) : super(context) {
        commonConstructor(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        commonConstructor(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        commonConstructor(context)
    }

    /////////////////////////
    private fun commonConstructor(context: Context) {
        setup()
    }

    private fun setup() {
        val layout = R.layout.simple_list_item_1
        val adapter = AutoCompleteAdapter(context!!, layout)
        setAdapter(adapter)

        onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (data != null) {
                if (data!!.size > position) {
                    if (position > -1) {
                        onValueSelected(data!![position])
                    }
                }
            }
        }
    }

    internal inner class AutoCompleteAdapter(context: Context, @LayoutRes resource: Int) :
        ArrayAdapter<String?>(context, resource), Filterable {
        override fun getCount(): Int {
            return data!!.size
        }


        override fun getItem(position: Int): String? {
            return itemResolver?.invoke(data,position)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    Log.d("filter_input","input=$constraint")
                    val results = FilterResults()
                    if (constraint != null) {
                        var conn: HttpURLConnection? = null
                        var input: InputStream? = null
                        try {
                            val url = urlComposer?.invoke(constraint.toString())?:URL("")
                            conn = url.openConnection() as HttpURLConnection
                            input = conn.inputStream
                            val reader = InputStreamReader(input, "UTF-8")
                            val buffer = BufferedReader(reader, 8192)
                            var line: String
                            val suggestions = ArrayList<String>()
                            /*while (buffer.readLine().also { line = it } != null) {
                                suggestions.add(line)
                            }*/
                            while (true){
                                var read = buffer.readLine()
                                if(read==null){
                                    break
                                }
                                else{
                                    suggestions.add(read)
                                }
                            }
                            results.values = suggestions
                            results.count = suggestions.size
                            data = processor?.invoke(suggestions)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        } finally {
                            if (input != null) {
                                try {
                                    input.close()
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }
                            conn?.disconnect()
                        }
                    }
                    return results
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged()
                    } else notifyDataSetInvalidated()
                }
            }
        }

        init {
            data = mutableListOf()
        }
    }
}