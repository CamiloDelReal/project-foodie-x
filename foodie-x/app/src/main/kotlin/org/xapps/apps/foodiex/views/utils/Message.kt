package org.xapps.apps.foodiex.views.utils


sealed class Message {

    data class Success(val data: Any? = null): Message()
    data class Error(val exception: Throwable): Message()
    object InternetAvailable: Message()
    object InternetNotAvailable: Message()
    object Loading: Message()
    object Loaded: Message()
    object HistoryLoaded: Message()
    object PopularDrinksLoaded: Message()
    object PopularMealsLoaded: Message()

}