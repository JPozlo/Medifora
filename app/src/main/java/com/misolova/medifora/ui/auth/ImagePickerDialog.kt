package com.misolova.medifora.ui.auth

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.misolova.medifora.R
import com.misolova.medifora.util.Constants
import com.misolova.medifora.util.Constants.RC_CAMERA_PERM
import com.misolova.medifora.util.Constants.RC_EXTERNAL_PERM
import kotlinx.android.synthetic.main.image_picker_dialog.view.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class ImagePickerDialog : DialogFragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    companion object {
        const val TAG = "IMAGE_PICKER_DIALOG"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.image_picker_dialog, container, false)

        rootView.btnCancelImagePicker.setOnClickListener {
            dismiss()
        }

        rootView.btnCameraImage.setOnClickListener {
            cameraTask()
            dismiss()
        }

        rootView.btnGalleryImage.setOnClickListener {
            galleryTask()
            dismiss()
        }

        return rootView
    }

    private fun captureImageWithCamera() {
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE)
    }

    private fun captureImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    private fun hasCameraPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)

    private fun hasExternalPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)

    fun cameraTask() {
        if (hasCameraPermission()) {
            captureImageWithCamera()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_reason_camera),
                RC_CAMERA_PERM,
                Manifest.permission.CAMERA
            )
        }
    }

    fun galleryTask() {
        if (hasExternalPermission()) {
            captureImageFromGallery()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_reason_external),
                RC_EXTERNAL_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Timber.d("$TAG: Permissions denied: $requestCode with perms: ${perms.size}")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.d("$TAG: Permissions granted: $requestCode with perms: ${perms.size}")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Timber.d("$TAG: Rationale Accepted: $requestCode")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Timber.d("$TAG: Rationale Denied: $requestCode")
    }
}