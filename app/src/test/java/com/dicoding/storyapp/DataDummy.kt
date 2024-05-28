package com.dicoding.storyapp

import com.dicoding.storyapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1683497415212_dnKjbXF-.jpg",
                "2023-05-07T22:10:15.213Z",
                "contoh1",
                "Halo",
                37.422092,
                "story-$i",
                -122.08392
            )
            items.add(quote)
        }
        return items
    }
}