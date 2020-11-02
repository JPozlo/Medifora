package com.misolova.medifora.ui.home.fragment


import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.misolova.medifora.R
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.showAlert
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class AccountFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            AccountFragment().apply {
                AccountFragment()
            }

        private const val TAG = "ACCOUNT FRAGMENT"
    }

    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = sharedPreferences.getString(KEY_USER_ID, "")!!
        tvDeleteAccount?.setOnClickListener {
            requireContext().showAlert("Delete Account", "Are you sure? All your information will be lost.")
                ?.setNegativeButton("Cancel"){dialog, which ->
                    dialog.cancel()
                }
                ?.setPositiveButton("Confirm"){dialog: DialogInterface?, which: Int ->
                    viewModel.deleteAcount(id)?.addOnSuccessListener {
                        notifyUser("Account successfully deleted")
//                        val action =
//                            findNavController().navigate(action)
                        dialog?.dismiss()

                    }?.addOnFailureListener { e ->
                        dialog?.dismiss()
                        notifyUser(e.localizedMessage!!)
                    }

                }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun notifyUser(message: String) = requireView().showSingleActionSnackbar(message)

}