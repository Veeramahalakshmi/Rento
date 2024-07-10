package com.example.loginsignup

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val itemName: String = "",
    val itemDetail: String = "",
    val itemPrice: Double = 0.0,
    val place: String = "",
    val category: String = "",
    val imageUrl: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString()?:"",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
        parcel.writeString(itemDetail)
        parcel.writeDouble(itemPrice)
        parcel.writeString(place)
        parcel.writeString(category)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
