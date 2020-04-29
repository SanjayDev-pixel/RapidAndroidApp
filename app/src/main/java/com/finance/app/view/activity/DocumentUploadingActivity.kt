package com.finance.app.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.afollestad.assent.Assent
import com.afollestad.assent.AssentCallback
import com.finance.app.R
import com.finance.app.databinding.ActivityDocumentUploadingBinding
import com.finance.app.persistence.model.DocumentTypeModel
import com.finance.app.persistence.model.KycDocumentModel
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.UploadedDocumentListAdapter
import com.finance.app.view.utils.getImageUriForImagePicker
import com.finance.app.view.utils.startFilePickerActivity
import com.finance.app.view.utils.startImagePickerActivity
import com.finance.app.workers.document.UploadDocumentWorker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import java.io.FileNotFoundException
import java.util.*
import javax.inject.Inject


class DocumentUploadingActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation

    private var screenTitle: String? = ""
    private var docCodeId: Int? = null
    private var applicantNumber: String? = null
    private var selectedDocumentUri: Uri? = null
    private var applicationDocumentID: String? = null

    private val presenter = Presenter()

    private val binding: ActivityDocumentUploadingBinding by ActivityBindingProviderDelegate(this , R.layout.activity_document_uploading)

    companion object {
        const val DOCUMENT_REQ_CODE = 1000
        const val DOCUMENT_IMAGE_REQ_CODE = 1001
        const val DOCUMENT_UPLOAD_BUNDLE = "document_upload_bundle"

        fun startActivity(context: Context , bundle: Bundle) {
            val intent = Intent(context , DocumentUploadingActivity::class.java)
            intent.putExtra(DOCUMENT_UPLOAD_BUNDLE , bundle)
            context.startActivity(intent)
        }
    }

    inner class DocumentTypeListRequest : ViewGeneric<Requests.RequestDocumentList , Response.ResponseDocumentList>(context = this) {
        override val apiRequest: Requests.RequestDocumentList?
            get() = getDocumentTypeListRequest()

        override fun getApiSuccess(value: Response.ResponseDocumentList) {
            hideProgressDialog()
            value.responseObj?.let { setDocumentTypeSpinner(it.documentTypes) } ?: kotlin.run {}
        }

        override fun getApiFailure(msg: String) {
            hideProgressDialog()
            Toast.makeText(this@DocumentUploadingActivity , msg , Toast.LENGTH_LONG).show()
        }
    }

    inner class UploadedDocumentListRequest : ViewGeneric<Requests.RequestUploadedDocumentList , Response.ResponseUploadedDocumentList>(context = this) {
        override val apiRequest: Requests.RequestUploadedDocumentList?
            get() = getUploadedDocumentListRequest()

        override fun getApiSuccess(value: Response.ResponseUploadedDocumentList) {
            binding.swipeLayoutDocument.isRefreshing = false
            value.responseObj?.let { response ->
                response.documents?.let { documentList ->
                    setUploadedDocumentListAdapter(documentList)
                }
            }
        }
    }

    inner class DownloadableDocumentLinkRequest(private val documentId: Int) : ViewGeneric<Requests.RequestDocumentDownloadableLink , Response.ResponseDocumentDownloadableLink>(context = this) {
        override val apiRequest: Requests.RequestDocumentDownloadableLink?
            get() = Requests.RequestDocumentDownloadableLink(documentId)
        override fun getApiSuccess(value: Response.ResponseDocumentDownloadableLink) {
            value.responseObj?.let { response ->
                response.documentPath?.let { url ->
                    val builder = CustomTabsIntent.Builder()
                    builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
                    builder.setShowTitle(false)
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(this@DocumentUploadingActivity , Uri.parse(url))
                }
            }
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        hideToolbar()
        hideSecondaryToolbar()
        setOnClickListener()

        getBundleData()

        setLeadNumber()
        setScreenTitle()

        fetchDocumentTypeList()
        fetchUploadedDocumentList()

    }

    private fun setLeadNumber() {
        binding.header.tvLeadNumber.text = "${LeadMetaData.getLeadData()?.leadNumber}"
    }

    private fun setScreenTitle() {
        binding.tvLabelTitle.text = screenTitle?.let { title -> "$title Uploaded Document" } ?: run { "Uploaded Document" }
    }

    private fun setOnClickListener() {
        binding.swipeLayoutDocument.setOnRefreshListener { fetchUploadedDocumentList() }
        binding.header.lytBack.setOnClickListener { onBackPressed() }
        binding.btnPickFile.setOnClickListener {
            Assent.requestPermissions(AssentCallback { result ->
                if (result.allPermissionsGranted())
                    showFileTypeChooserDialog()
            } , 1 , Assent.WRITE_EXTERNAL_STORAGE , Assent.CAMERA)

        }
        binding.btnUpload.setOnClickListener { if (isKycDocumentDetailValid()) saveKycDocumentIntoDatabase() }
    }

    private fun setDocumentTypeSpinner(documentTypes: ArrayList<DocumentTypeModel>) {
        val adapter = ArrayAdapter<DocumentTypeModel>(this , android.R.layout.simple_spinner_item , documentTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDocumentType.adapter = adapter
        binding.spinnerDocumentType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                if(position==0) {
                    var str = parent.getItemAtPosition(position).toString()
                    binding.etDocumentName.setText(str + "_" + applicantNumber)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}

        }



    }

    private fun setUploadedDocumentListAdapter(documentList: ArrayList<DocumentTypeModel>) {
        val adapter = UploadedDocumentListAdapter(documentList)
        binding.rvUploadedDocumentList.adapter = adapter
        adapter.setOnItemClickListener(object : UploadedDocumentListAdapter.ItemClickListener {
            override fun onKycDetailDownloadClicked(position: Int , documentTypeModel: DocumentTypeModel) {
                documentTypeModel.applicationDocumentID?.let { id ->
                    presenter.callNetwork(ConstantsApi.CALL_DOWNLOAD_DOCUMENT , dmiConnector = DownloadableDocumentLinkRequest(id))
                }
            }

            override fun onKycDetailDeleteClicked(position: Int , documentTypeModel: DocumentTypeModel) {
            }
        })
    }

    private fun getBundleData() {
        val bundle = intent.getBundleExtra(DOCUMENT_UPLOAD_BUNDLE)
        bundle?.let {
            screenTitle = it.getString(Constants.KEY_TITLE)
            docCodeId = it.getInt(Constants.KEY_DOC_ID)
            applicantNumber = it.getString(Constants.KEY_APPLICANT_NUMBER)
            if (it.containsKey(Constants.KEY_FORM_ID)) {
                applicationDocumentID = it.getString(Constants.KEY_FORM_ID)
            }
        }
    }

    private fun fetchDocumentTypeList() {
        showProgressDialog()
        if (docCodeId != null)
            presenter.callNetwork(ConstantsApi.CALL_DOC_TYPE , dmiConnector = DocumentTypeListRequest())
    }

    private fun fetchUploadedDocumentList() {
        if (LeadMetaData.getLeadId() != null && docCodeId != null)
            presenter.callNetwork(ConstantsApi.CALL_UPLOADED_DOC , dmiConnector = UploadedDocumentListRequest())
    }

    private fun getDocumentTypeListRequest(): Requests.RequestDocumentList? {
        return docCodeId?.let { Requests.RequestDocumentList(it) }
    }

    private fun getUploadedDocumentListRequest(): Requests.RequestUploadedDocumentList? {
        return LeadMetaData.getLeadId()?.let { docCodeId?.let { codeId -> Requests.RequestUploadedDocumentList(codeId , it) } }
    }

    private fun onClearSelectedDocumentDetails() {
        binding.spinnerDocumentType.setSelection(0)
        binding.etDocumentName.setText("")
        binding.tvFileSizeErrorLabel.visibility = View.INVISIBLE
        selectedDocumentUri = null
    }

    private fun showFileTypeChooserDialog() {
        val actionList = arrayOf(Constants.ACTION_PICK_FILE , Constants.ACTION_TAKE_IMAGE)
        val builder = AlertDialog.Builder(this@DocumentUploadingActivity)
        builder.setTitle("Choose Action!")
        builder.setItems(actionList) { _ , which ->
            when (actionList[which]) {
                Constants.ACTION_PICK_FILE -> this@DocumentUploadingActivity.startFilePickerActivity(DOCUMENT_REQ_CODE)
                Constants.ACTION_TAKE_IMAGE -> {
                    selectedDocumentUri = this@DocumentUploadingActivity.getImageUriForImagePicker("doc_image_${Date().time}")
                    selectedDocumentUri?.let { uri ->
                        this@DocumentUploadingActivity.startImagePickerActivity(DOCUMENT_IMAGE_REQ_CODE , uri)
                    }
                }
            }
        }
        builder.show()
    }

    private fun onDocumentSelected(data: Intent?) {
        data?.let {
            selectedDocumentUri = it.data
            binding.btnPickFile.error = null
        }
    }

    private fun onImageSelected(data: Uri?) {
        data?.let {
            binding.btnPickFile.error = null
        }
    }

    private fun isKycDocumentDetailValid(): Boolean {
        if (selectedDocumentUri == null) {
            binding.btnPickFile.error = "Please choose document file"
            return false
        }

        try {
            val totalFileSizeInBytes = contentResolver?.openAssetFileDescriptor(selectedDocumentUri!! , "r")?.length
            totalFileSizeInBytes?.let { size ->
                if (size >= Constants.FILE_SIZE_ALLOWED) {
                    binding.tvFileSizeErrorLabel.visibility = View.VISIBLE
                    return false
                }
            }
        } catch (exp: FileNotFoundException) {
            return false
        }

        return formValidation.validateKycDocumentDetail(binding)
    }

    private fun saveKycDocumentIntoDatabase() {
        val kycDocumentModel = KycDocumentModel()
        kycDocumentModel.leadID = LeadMetaData.getLeadId()
        kycDocumentModel.leadApplicantNumber = applicantNumber
        kycDocumentModel.applicationDocumentID = applicationDocumentID
        kycDocumentModel.documentID = (binding.spinnerDocumentType.selectedItem as DocumentTypeModel?)?.documentID
        kycDocumentModel.documentName = binding.etDocumentName.text.toString()
        kycDocumentModel.document = selectedDocumentUri.toString()

        //Now Save data into database,then start worker...
        val handler = Handler(Looper.getMainLooper())
        GlobalScope.launch {
            dataBase.provideDataBaseSource().kycDocumentDao().add(kycDocumentModel)
            startDocumentWorkerTask()
            handler.post {
                Toast.makeText(this@DocumentUploadingActivity , "Document Saved Successfully" , Toast.LENGTH_LONG).show()
            }
        }

        //Now Clear the screen...
        onClearSelectedDocumentDetails()
    }


    private fun startDocumentWorkerTask() {
        val mWorkManager = WorkManager.getInstance()
        val mRequest = OneTimeWorkRequest.Builder(UploadDocumentWorker::class.java).build()
        mWorkManager.enqueue(mRequest)
    }

    override fun onActivityResult(requestCode: Int , resultCode: Int , data: Intent?) {
        when (requestCode) {
            DOCUMENT_REQ_CODE -> if (resultCode == Activity.RESULT_OK) onDocumentSelected(data) else selectedDocumentUri = null
            DOCUMENT_IMAGE_REQ_CODE -> if (resultCode == Activity.RESULT_OK) onImageSelected(selectedDocumentUri) else selectedDocumentUri = null
            else -> Toast.makeText(this@DocumentUploadingActivity , "Did not pick image, please try again!" , Toast.LENGTH_LONG).show()
        }
    }


}
