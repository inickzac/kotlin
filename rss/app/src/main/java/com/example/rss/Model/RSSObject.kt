package com.example.rss.Model

import com.example.rss.Feed
import com.example.rss.Item

data class RSSObject(
    val status: String,
    val feed: Feed,
    var items: List<Item>
)
