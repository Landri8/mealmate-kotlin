package com.jenny.mealmate.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.usecases.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
): ViewModel(){

    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.SaveAppEntry -> {
                saveAppEntry()
            }
        }
    }

    private fun saveAppEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }

}