package com.jolufeja.tudas

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentChallengeReceivedInfoBinding
import com.jolufeja.tudas.service.challenges.ChallengeService
import com.jolufeja.tudas.service.challenges.ProofKind
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File


sealed class ChallengeErrors(reason: String) : Throwable(reason)
object FailedToGetChallenge : ChallengeErrors("Couldn't retrieve challenge from backend.")
object NonExistentChallenge : ChallengeErrors("No challenge associated with given name found.")
object MissingChallengeName : ChallengeErrors("No challenge name passed to fragment.")


class IndividualChallengeReceivedFragment(
    private val challengeService: ChallengeService
) :
    DataBoundFragment<IndividualChallengeReceivedViewModel, FragmentChallengeReceivedInfoBinding>(
        R.layout.fragment_challenge_received_info,
        IndividualChallengeReceivedViewModel::class,
        BR.challengeSentViewModel
    ) {

    companion object {
        const val CHALLENGE_KEY = "challenge"
        private const val REQUEST_CODE_CAMERA = 1
        private const val REQUEST_CODE_IMAGE = 2
        private const val FILE_INTENT_TYPE = "image/*"
    }

    private lateinit var filePhoto: File
    private lateinit var takenImage: Bitmap
    private lateinit var viewImage: ImageView

    override val viewModel: IndividualChallengeReceivedViewModel by viewModel {
        val challenge = arguments?.getSerializable(CHALLENGE_KEY) ?: throw MissingChallengeName
        parametersOf(challenge)
    }

    override fun createBinding(view: View) = FragmentChallengeReceivedInfoBinding.bind(view)

    private fun getCamera() {
        if ((activity as MainActivity?)!!.hasNoPermissions()) {
            (activity as MainActivity?)!!.requestPermission()
        }
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            takePhotoIntent,
            REQUEST_CODE_CAMERA
        );
    }

//     Listener for Back Button to close fragment
//    backButton.setOnClickListener {
//        requireActivity().supportFragmentManager.popBackStack();
//    }

    private fun getImage() {
        if ((activity as MainActivity?)!!.hasNoPermissions()) {
            (activity as MainActivity?)!!.requestPermission()
        }
        val fileIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileIntent.type = FILE_INTENT_TYPE
        startActivityForResult(fileIntent, REQUEST_CODE_IMAGE);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            takenImage = data?.extras?.get("data") as Bitmap
            viewImage.setImageBitmap(takenImage);
        }
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            viewImage.setImageURI(data?.data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewAndBindingCreated(
        view: View,
        binding: FragmentChallengeReceivedInfoBinding,
        savedInstanceState: Bundle?
    ) {

        binding.addFile.setOnClickListener { getImage() }
        binding.openCamera.setOnClickListener { getCamera() }

        /*
        Only try to complete the challenge if the user has selected an image already.
         */
        lifecycleScope.launch {
            viewModel.completeChallenge.receiveAsFlow().collect { finishChallenge ->
                val proof = when {
                    ::takenImage.isInitialized -> ProofKind.ProofImage(takenImage)
                    ::filePhoto.isInitialized -> {
                        val bytes = filePhoto.readBytes()
                        ProofKind.ProofImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                    }
                    else -> null
                }

                proof?.let { proofKind ->
                    challengeService.finishChallengeWithProof(
                        finishChallenge,
                        proofKind
                    ).fold(
                        ifLeft = { err -> error(err) },
                        ifRight = { requireActivity().supportFragmentManager.popBackStack() }
                    )
                }
            }
        }
    }
}