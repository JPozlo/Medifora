package com.misolova.medifora.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.misolova.medifora.R
import com.misolova.medifora.util.Constants.FEEDBACK_USER_SUBJECT
import com.misolova.medifora.util.Constants.OFFICIAL_FEEDBACK_EMAIL
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbackFragment : Fragment() {
    companion object {
        private const val TAG = "FEEDBACK_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return container?.inflate(R.layout.fragment_feedback, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextMessage = view.findViewById(R.id.etFeedbackMessage) as EditText?
//        val editTextEmail = view.findViewById(R.id.etFeedbackEmail) as EditText?

        btnSendFeedback?.setOnClickListener {
            val message = editTextMessage?.text.toString()
//            val email  = editTextEmail?.text.toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, OFFICIAL_FEEDBACK_EMAIL)
            intent.putExtra(Intent.EXTRA_SUBJECT, FEEDBACK_USER_SUBJECT)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "message/rfc822"
            startActivity(Intent.createChooser(intent, "Select email"))
        }
    }


}