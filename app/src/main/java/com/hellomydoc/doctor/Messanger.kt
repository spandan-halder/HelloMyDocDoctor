package com.hellomydoc.doctor

data class Message(
    val key: String,
    val data: Any? = null
)
class Messanger {
    internal data class Subscription(
        val id: String,
        val channel: String,
        val callback: ((Message?) -> Unit)?
    )
    private var subscritions: MutableList<Subscription> = mutableListOf()
    companion object{
        private var instance: Messanger? = null
        fun initialize(){
            if(instance==null){
                instance = Messanger()
            }
        }

        fun subscribe(subscriberId: String,subscribeChannel: String,callback: ((Message?)->Unit)?){
            instance?.subscritions?.add(Subscription(
                subscriberId,
                subscribeChannel,
                callback
            ))
        }

        fun cancel(subscriberId: String,subscribeChannel: String?=null){
            instance?.subscritions?.removeAll {
                if(subscribeChannel!=null){
                    (it.id==subscriberId)&&(it.channel==subscribeChannel)
                }
                else{
                    it.id==subscriberId
                }
            }
        }

        fun publish(subscribeChannel: String,data: Message?=null){
            instance?.subscritions?.filter {
                it.channel==subscribeChannel
            }?.forEach {
                it.callback?.invoke(data)
            }
        }
    }
}