package com.example.chattutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.getstream.sdk.chat.viewmodel.channels.ChannelsViewModelImpl
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.utils.FilterObject
import io.getstream.chat.android.livedata.ChatDomain

@Suppress("UNCHECKED_CAST")
// TODO: move to library?
class ChannelsViewModelFactory(private val filter: FilterObject, private val sort: QuerySort): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ChannelsViewModelImpl(ChatDomain.instance(), filter, sort) as T
}