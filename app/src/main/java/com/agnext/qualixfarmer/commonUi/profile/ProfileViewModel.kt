package com.agnext.qualixfarmer.commonUi.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class ProfileViewModel (val profileInteracter: ProfileInteracter) : ViewModel(),
    OnProfileFinishedListener
{
     /**Observable*/
     private val _profileState: MutableLiveData<ScreenState<ProfileState>> = MutableLiveData()
    val profileState: LiveData<ScreenState<ProfileState>>
        get() = _profileState


    /**Data for profile*/
    private val _profile: MutableLiveData<ProfileData> = MutableLiveData()
    val profile: LiveData<ProfileData>
        get() = _profile


    /**Forward flow*/
    fun getProfile()
    {
        _profileState.value = ScreenState.Loading
        profileInteracter.getProfile(this)
    }

    fun editProfile(userId:String)
    {
        _profileState.value = ScreenState.Loading
        profileInteracter.editProfile(userId,this)
    }

    /**Backward flow*/
    override fun onGetProfileSuccess() {
        _profileState.value = ScreenState.Render(ProfileState.getProfileSuccess)
    }

    override fun onGetProfileFailure() {
        _profileState.value = ScreenState.Render(ProfileState.getProfileFailure)
    }

    override fun onEditProfileSuccess() {
        _profileState.value = ScreenState.Render(ProfileState.editProfileSuccess)
    }

    override fun onEditProfileFailure() {
        _profileState.value = ScreenState.Render(ProfileState.getProfileFailure)
    }
}
class ProfileViewModelFactory(private val profileInteracter: ProfileInteracter) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(profileInteracter) as T
    }}