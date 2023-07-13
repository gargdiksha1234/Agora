package com.project.agora

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.project.agora.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var agora: Agora? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        agora= Agora(this)
        initView()
        val name=agora?.initSDK()
        binding.tvUsername.text=name
        Log.d("gggghv",name.toString())
    }
    private fun initView() {
        binding.tvLog.movementMethod = ScrollingMovementMethod()
    }

    override fun onResume() {
        super.onResume()
        agora?.initListener()
    }

    // Logs in with a token.
    fun signInWithToken(view: View?) {
        agora?.loginToAgora()
    }

    // Logs out.
    fun signOut(view: View?) {
        agora?.signout()
    }
    // Sends the first message.
    fun sendFirstMessage(view: View?) {
        val toSendName =binding.etToChatName.text.toString()
            .trim { it <= ' ' }
        val content =
            binding.etMsgContent.text.toString().trim { it <= ' ' }
        // Creates a text message.
        agora?.sendMsg(content,toSendName)

    }
    fun fetchConversation(view: View?){
        agora?.fetchConversations()
    }
    fun fetchMessageLsit(view: View?){
        agora?.fetchMessagesFromConversation()
    }


}