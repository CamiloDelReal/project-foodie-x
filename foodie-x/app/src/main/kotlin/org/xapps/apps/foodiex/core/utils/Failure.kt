package org.xapps.apps.foodiex.core.utils


sealed class Failure {

    data class Exception(val description: String?): Failure()

    object Database: Failure()

}