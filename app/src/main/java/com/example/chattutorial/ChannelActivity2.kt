package com.example.chattutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.getstream.sdk.chat.view.common.visible
import com.getstream.sdk.chat.viewmodel.ChannelHeaderViewModel
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.bindView
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import com.getstream.sdk.chat.viewmodel.messages.bindView
import io.getstream.chat.android.client.models.Channel
import kotlinx.android.synthetic.main.activity_channel_2.*

/**
 * Show the messages for a channel
 *
 */
class ChannelActivity2 : AppCompatActivity(R.layout.activity_channel_2) {

    private val cid by lazy {
        intent.getStringExtra(CID_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageListView.setViewHolderFactory(MyMessageViewHolderFactory())
        val messagesViewModel = MessageListViewModel(cid)
            .apply {
                bindView(messageListView, this@ChannelActivity2)
                state.observe(
                    this@ChannelActivity2,
                    {
                        when (it) {
                            is MessageListViewModel.State.Loading -> progressBar.visible(true)
                            is MessageListViewModel.State.Result -> progressBar.visible(false)
                            is MessageListViewModel.State.NavigateUp -> finish()
                        }
                    }
                )
            }

        ChannelHeaderViewModel(cid).bindView(channelHeaderView, this)

        MessageInputViewModel(cid).apply {
            bindView(messageInputView, this@ChannelActivity2)
            messagesViewModel.mode.observe(
                this@ChannelActivity2,
                {
                    when (it) {
                        is MessageListViewModel.Mode.Thread -> setActiveThread(it.parentMessage)
                        is MessageListViewModel.Mode.Normal -> resetThread()
                    }
                }
            )
            messageListView.setOnMessageEditHandler {
                editMessage.postValue(it)
            }
        }
        val backButtonHandler = {
            messagesViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
        }
        channelHeaderView.onBackClick = { backButtonHandler() }
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backButtonHandler()
                }
            }
        )
    }

    companion object {
        private const val CID_KEY = "key:cid"

        fun newIntent(context: Context, channel: Channel) =
            Intent(context, ChannelActivity2::class.java).apply {
                putExtra(CID_KEY, channel.cid)
            }
    }
}