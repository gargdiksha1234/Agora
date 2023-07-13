package com.project.agora

import android.content.Context
import android.util.Log
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.ValueCallBack
import io.agora.chat.*
import io.agora.exceptions.ChatException


class Agora(private val context: Context) {
    private val TAG=Agora::class.java.simpleName
    private val USERNAME: String? = "f306b79c52024f1bac4e51c7d84aaa48"
    private val TOKEN = "007eJxTYFizRNdmi/7Mokd7Rf5msyoUrVvhdrlH4t+H1WKXv54NvW2gwJBmbGCWZG6ZbGpkYGSSZpiUmGySamqYbJ5iYZKYmGhicefm+pSGQEaG5z1FTIwMrAyMQAjiqzBYGFmYmKSaGegapiUZ6xoapqbqJpmYWeqaGJsYJVtamFkamlgCAEyjKUo="
    private val APP_KEY = "61999433#1165990"
    private var mConversationMap: Map<String?, Conversation?>? = null



    fun initSDK():String {
        val options = ChatOptions()
        //   Gets your App Key from Agora Console.
        // Sets your App Key to options.
        options.appKey = APP_KEY
        options.requireAck=true
//        options.setRequireDeliveryAck=true

        // Initializes the Agora Chat SDK.
        ChatClient.getInstance().init(context, options)
        // Makes the Agora Chat SDK debuggable.
        ChatClient.getInstance().setDebugMode(true)
        // Shows the current user.
        Log.e(TAG, "initSDK: ")
        //  (findViewById<View>(R.id.tv_username) as TextView).text
//     return "Current user: $USERNAME"
        return context.getString(R.string.Current_user,USERNAME)

    }
    fun sendMsg(content: String, toSendName: String) {
        val message = ChatMessage.createTextSendMessage(content, toSendName)
        // Sets the message callback before sending the message.
        message.setMessageStatusCallback(object : CallBack {
            override fun onSuccess() {
                Log.e("Send message success!", true.toString())
                Log.e(TAG, "onSuccess: hytytyt")

            }

            override fun onError(code: Int, error: String) {
                Log.e(error, true.toString())
            }
        })

        // Sends the message.
        ChatClient.getInstance().chatManager().sendMessage(message)


    }
    fun signout()
    {

        if (ChatClient.getInstance().isLoggedInBefore) {
            ChatClient.getInstance().logout(true, object : CallBack {
                override fun onSuccess() {
                    Log.e("Sign out success!", true.toString())
                }

                override fun onError(code: Int, error: String) {
                    Log.e("error", error)
                }
            })
        } else {
            Log.e("You were not logged in", false.toString())
        }
    }
    fun loginToAgora() {
        ChatClient.getInstance().loginWithAgoraToken(this.USERNAME, TOKEN, object : CallBack {
            override fun onSuccess() {
                Log.e("Sign in success!", true.toString())
//                val roomName = "MyChatRoom"
                Thread {

                    val caht = ChatClient.getInstance().chatroomManager().createChatRoom(
                        "t1", "hsdgjhs", "helllllo", 2,
                        mutableListOf("ddh", "cnmjsdc")
                    )
                    Log.e(TAG, "onSuccess: ${caht}")
                }

            }

            override fun onError(code: Int, error: String) {
                Log.e("error", error)
            }
        })
    }
    fun initListener() {
        ChatClient.getInstance().chatManager().addMessageListener { messages: List<ChatMessage> ->
            for (message in messages) {
                val builder = StringBuilder()
                builder.append("Receive a ").append(message.type.name)
                    .append(" message from: ").append(message.from)
                if (message.type == ChatMessage.Type.TXT) {
                    builder.append(" content:")
                        .append((message.body as TextMessageBody).message)
                }
                Log.e(TAG, "initListener: ${builder.toString()}}")
            }

        }
        // Adds connection event callbacks.
        ChatClient.getInstance().addConnectionListener(object : ConnectionListener {
            override fun onConnected() {
                Log.e("onConnected", false.toString())
            }

            override fun onDisconnected(error: Int) {
                Log.e("onDisconnected: $error", false.toString())
            }

            override fun onLogout(errorCode: Int) {
                Log.e("User needs to log out: $errorCode", false.toString())
                ChatClient.getInstance().logout(false, null)
            }

            // This callback occurs when the token expires. When the callback is triggered, the app client must get a new token from the app server and logs in to the app again.
            override fun onTokenExpired() {
                Log.e("onTokenExpired", true.toString())
            }

            // This callback occurs when the token is about to expire.
            override fun onTokenWillExpire() {
              //  getTokenFromAppServer(RENEW_TOKEN)

                Log.e("onTokenWillExpire", true.toString())
            }
        })
    }
     fun fetchConversations() {
        ChatClient.getInstance().chatManager()
            .asyncFetchConversationsFromServer(object : ValueCallBack<Map<String?, Conversation>> {
                override fun onSuccess(conversationMap: Map<String?, Conversation>) {
                    Log.e(TAG, "222: ${conversationMap.size}")
                    if (conversationMap.size > 0) {
                        mConversationMap = conversationMap
                        val iterator = conversationMap.entries.iterator()
                        if (iterator.hasNext()) {
                            val (_, conv) = iterator.next()
                            Log.e(TAG, "onSuccess: ${conv.type.name}")
                        }
                    }
                }

                override fun onError(code: Int, error: String) {
                    Log.e(TAG, "onError: ${error}")
                }
            })
    }

    /**
     * Fetch messages from Agora Chat Server by conversation id
     */
     fun fetchMessagesFromConversation() {
        val size = mConversationMap?.size?:0
        val target = (Math.random() * size).toInt()
        var index = 0
        var targetConversation: Conversation? = null
        for (conversation in mConversationMap!!.values) {
            if (target == index) {
                targetConversation = conversation
                break
            }
            index++
        }
        if (targetConversation == null) {
            return
        }
        ChatClient.getInstance().chatManager()
            .asyncFetchHistoryMessage(targetConversation.conversationId(),
                targetConversation.type,
                20,
                null,
                object : ValueCallBack<CursorResult<ChatMessage>> {
                    override fun onSuccess(result: CursorResult<ChatMessage>) {

                        val messages = result.data
                        Log.e(TAG, "onSuccess111: ${messages.size}")
                        for (i in messages.indices) {

                            val message = messages[i]
                            Log.e(
                                TAG,
                                "onSuccess: ${message.msgId + " " + message.body + " " + message.to}"
                            )
                        }
                        try {
                            var ddd=  ChatClient.getInstance().chatManager().ackConversationRead(targetConversation.conversationId())
                            Log.e(TAG, "fetchMessagesFromConversation222: ${ddd}", )
                        } catch (e: ChatException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(code: Int, error: String) {
                        Log.e(TAG, "onError: ${error}")
                    }
                })

    }

}