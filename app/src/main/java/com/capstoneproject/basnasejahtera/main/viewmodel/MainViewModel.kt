package com.capstoneproject.basnasejahtera.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstoneproject.basnasejahtera.model.DataIDRumah
import com.capstoneproject.basnasejahtera.model.DataKonsumen
import com.capstoneproject.basnasejahtera.model.UserModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getDataUserKonsumen(): LiveData<DataKonsumen> {
        return pref.getDataUserKonsumen().asLiveData()
    }

    fun getIDRumah(): LiveData<DataIDRumah> {
        return pref.getIDRumah().asLiveData()
    }

    fun saveIDRumah(id: Int?) {
        viewModelScope.launch {
            if (id != null) {
                pref.saveIDRumah(id)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}