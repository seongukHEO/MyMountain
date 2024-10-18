package com.example.my_mountain.model


import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml (name = "response")
data class WeatherApiModel(
    @Element(name = "body")
    val body: Body?,
    @Element(name = "header")
    val header: Header?
)

@Xml (name = "header")
data class Header(
    @PropertyElement (name = "resultCode")
    val resultCode: Int?,
    @PropertyElement (name = "resultMsg")
    val resultMsg: String?
)

@Xml (name = "body")
data class Body(
    @Element (name = "items")
    val items: Items?,
    @PropertyElement (name = "numOfRows")
    val numOfRows: Int?,
    @PropertyElement (name = "pageNo")
    val pageNo: Int?,
    @PropertyElement (name = "totalCount")
    val totalCount:Int?
)

@Xml(name = "items")
data class Items(
    @PropertyElement(name = "item")
    val item: List<Item>?
)

@Xml(name = "Item")
data class Item(
    @PropertyElement(name = "cprn")
    val cprn:String?,

    @PropertyElement (name = "hm10m")
    val hm10m: String?,

    @PropertyElement (name = "hm2m")
    val hm2m: String?,

    @PropertyElement(name = "obsname")
    val obsname: String?,

    @PropertyElement(name = "tm")
    val tm: String?,

    @PropertyElement(name = "tm10m")
    val tm10m: String?,

    @PropertyElement(name = "tm2m")
    val tm2m:String?,

    @PropertyElement(name = "ts")
    val ts:String?,

    @PropertyElement(name = "ws10m")
    val ws10m: String?,

    @PropertyElement(name = "ws2m")
    val ws2m: String?
)

