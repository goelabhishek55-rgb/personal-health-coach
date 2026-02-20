package com.personalcoach.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.model.UserProfile
import com.personalcoach.domain.repository.UserProfileRepository
import com.personalcoach.domain.usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    val userProfile: StateFlow<UserProfile?> = userProfileRepository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun toggleHealthConnect(enabled: Boolean) {
        viewModelScope.launch {
            userProfile.value?.let {
                saveUserProfileUseCase(it.copy(samsungHealthEnabled = enabled))
            }
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            userProfile.value?.let {
                saveUserProfileUseCase(it.copy(name = name))
            }
        }
    }
}
