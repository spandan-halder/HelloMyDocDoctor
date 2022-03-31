package com.hellomydoc.doctor.data

import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class Resp<T>(
    val response: Response<T>
){
    val isError: Boolean
        get(){
            return response.errorBody()!=null
        }
    val errorMessage: String
        get(){
            val inputStream = response.errorBody()?.byteStream()
            val textBuilder = StringBuilder()
            BufferedReader(
                InputStreamReader(inputStream,
                Charset.forName(StandardCharsets.UTF_8.name()))
            ).use { reader ->
                var c = 0
                while (reader.read().also { c = it } != -1) {
                    textBuilder.append(c.toChar())
                }
            }
            return textBuilder.toString()
        }
    val isNotError: Boolean
        get(){
            return response.errorBody()==null
        }
    val isSuccess: Boolean
        get(){
            return response.isSuccessful
        }
    val body: T?
        get() = response.body()
}

val <T> Response<T>.resp: Resp<T>
    get() = Resp(this)