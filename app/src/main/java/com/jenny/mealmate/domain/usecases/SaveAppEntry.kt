package com.jenny.mealmate.domain.usecases

import com.jenny.mealmate.domain.manager.LocalUserManager
import javax.inject.Inject

class SaveAppEntry @Inject constructor (
    private val localUserManager: LocalUserManager
){

    suspend operator fun invoke() {
        localUserManager.saveAppEntry()
    }

}