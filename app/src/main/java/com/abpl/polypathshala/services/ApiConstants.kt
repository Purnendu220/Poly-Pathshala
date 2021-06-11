package com.abpl.polypathshala.services

interface ApiConstants {
    interface Refrences {
        companion object {
            const val USER_REFRENCE = "USERS"
            const val USER_NORMAL = "NORMAL"
            const val USER_ADMIN = "ADMIN"

            const val PRODUCT_REFRENCE = "PRODUCT_REFRENCE"
            const val USER_ADDRESS_REFRENCE = "ADDRESSES"
            const val USER_PROPERTY_REFERENCE = "PROPERTIES"
            const val ADMIN_PROPERTY_REFERENCE = "ADMIN_PROPERTIES"
            const val USER_WISHLIST_REFERENCE = "WHISLISTS"
            const val USER_CART_REFERENCE = "USER_CART"
        }
    }

    interface Prefrences {
        companion object {
            const val USER_KEY = "USER_DATA"
            const val PRODUCT_LIST_KEY = "PRODUCT_LIST_KEY"
            const val CART_LIST_KEY = "CART_LIST_KEY"
            const val ADDRESS_LIST_KEY = "ADDRESS_LIST_KEY"
            const val TOKEN = "TOKEN"
        }
    }
}