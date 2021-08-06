package org.xapps.apps.foodiex.views.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.xapps.apps.foodiex.core.repositories.RecipesRepository
import org.xapps.apps.foodiex.views.utils.Message
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
): ViewModel() {

    private val messageFlow: MutableSharedFlow<Message> = MutableSharedFlow(replay = 1)

    val message: SharedFlow<Message> = messageFlow

}