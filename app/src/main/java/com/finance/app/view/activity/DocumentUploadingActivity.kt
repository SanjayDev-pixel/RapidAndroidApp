package com.finance.app.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.finance.app.R
import com.finance.app.databinding.ActivityDocumentUploadingBinding
import com.finance.app.persistence.model.DocumentTypeModel
import com.finance.app.persistence.model.KycDocumentModel
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.UploadedDocumentListAdapter
import com.finance.app.view.utils.startFilePickerActivity
import com.finance.app.workers.Workers.document.UploadDocumentWorker
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
import javax.inject.Inject


class DocumentUploadingActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation

    private var docCodeId: Int? = null
    private var applicantNumber: String? = null
    private var selectedDocumentUri: Uri? = null

    private val presenter = Presenter()

    private val binding: ActivityDocumentUploadingBinding by ActivityBindingProviderDelegate(this, R.layout.activity_document_uploading)

    companion object {
        const val DOCUMENT_REQ_CODE = 1000
        const val DOCUMENT_UPLOAD_BUNDLE = "document_upload_bundle"

        fun startActivity(context: Context, bundle: Bundle) {
            val intent = Intent(context, DocumentUploadingActivity::class.java)
            intent.putExtra(DOCUMENT_UPLOAD_BUNDLE, bundle)
            context.startActivity(intent)
        }
    }

    inner class DocumentTypeListRequest : ViewGeneric<Requests.RequestDocumentList, Response.ResponseDocumentList>(context = this) {
        override val apiRequest: Requests.RequestDocumentList?
            get() = getDocumentTypeListRequest()

        override fun getApiSuccess(value: Response.ResponseDocumentList) {
            hideProgressDialog()
            value.responseObj?.let { setDocumentTypeSpinner(it.documentTypes) } ?: kotlin.run {}
        }

        override fun getApiFailure(msg: String) {
            hideProgressDialog()
            Toast.makeText(this@DocumentUploadingActivity, msg, Toast.LENGTH_LONG).show()
        }
    }

    inner class UploadedDocumentListRequest : ViewGeneric<Requests.RequestUploadedDocumentList, Response.ResponseUploadedDocumentList>(context = this) {
        override val apiRequest: Requests.RequestUploadedDocumentList?
            get() = getUploadedDocumentListRequest()

        override fun getApiSuccess(value: Response.ResponseUploadedDocumentList) {
            value.responseObj?.let { response ->
                response.documents?.let { documentList ->
                    setUploadedDocumentListAdapter(documentList)
                }
            }
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        hideToolbar()
        hideSecondaryToolbar()
        setOnClickListener()

        fetchBundleData()
        fetchDocumentTypeList()
        fetchUploadedDocumentList()
    }

    private fun setOnClickListener() {
        binding.header.lytBack.setOnClickListener { onBackPressed() }
        binding.btnPickFile.setOnClickListener {
            this.startFilePickerActivity(DOCUMENT_REQ_CODE)
        }
        binding.btnUpload.setOnClickListener { if (isKycDocumentDetailValid()) saveKycDocumentIntoDatabase() }
    }

    private fun setDocumentTypeSpinner(documentTypes: ArrayList<DocumentTypeModel>) {
        val adapter = ArrayAdapter<DocumentTypeModel>(this, android.R.layout.simple_spinner_item, documentTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDocumentType.adapter = adapter
    }

    private fun setUploadedDocumentListAdapter(documentList: ArrayList<DocumentTypeModel>) {
        val adapter = UploadedDocumentListAdapter(documentList)
        binding.rvUploadedDocumentList.adapter = adapter
        adapter.setOnItemClickListener(object : UploadedDocumentListAdapter.ItemClickListener {
            override fun onKycDetailDeleteClicked(position: Int) {
            }
        })
    }

    private fun fetchBundleData() {
        val bundle = intent.getBundleExtra(DOCUMENT_UPLOAD_BUNDLE)
        bundle?.let {
            docCodeId = it.getInt(Constants.KEY_DOC_ID)
            applicantNumber = it.getString(Constants.KEY_APPLICANT_NUMBER)
        }
    }

    private fun fetchDocumentTypeList() {
        showProgressDialog()
        if (docCodeId != null)
            presenter.callNetwork(ConstantsApi.CALL_DOC_TYPE, dmiConnector = DocumentTypeListRequest())
    }

    private fun fetchUploadedDocumentList() {
        if (LeadMetaData.getLeadId() != null && docCodeId != null)
            presenter.callNetwork(ConstantsApi.CALL_UPLOADED_DOC, dmiConnector = UploadedDocumentListRequest())
    }

    private fun getDocumentTypeListRequest(): Requests.RequestDocumentList? {
        return docCodeId?.let { Requests.RequestDocumentList(it) }
    }

    private fun getUploadedDocumentListRequest(): Requests.RequestUploadedDocumentList? {
        return LeadMetaData.getLeadId()?.let { docCodeId?.let { codeId -> Requests.RequestUploadedDocumentList(codeId, it) } }
    }

    private fun onDocumentSelected(data: Intent?) {
        data?.let {
            selectedDocumentUri = it.data
            binding.btnPickFile.error = null
        }
    }

    private fun isKycDocumentDetailValid(): Boolean {
        if (selectedDocumentUri == null) {
            binding.btnPickFile.error = "Please choose document file"
            return false
        }
        return formValidation.validateKycDocumentDetail(binding)
    }

    private fun saveKycDocumentIntoDatabase() {
        val kycDocumentModel = KycDocumentModel()
        kycDocumentModel.leadID = LeadMetaData.getLeadId()
        kycDocumentModel.leadApplicantNumber = applicantNumber
        kycDocumentModel.documentID = (binding.spinnerDocumentType.selectedItem as DocumentTypeModel?)?.documentID
//        kycDocumentModel.isSyncWithServer = false
        kycDocumentModel.documentName = binding.etDocumentName.text.toString()
        kycDocumentModel.document = selectedDocumentUri.toString()

        //Now Save data into database,then start worker...
        val handler = Handler(Looper.getMainLooper())
        GlobalScope.launch {
            dataBase.provideDataBaseSource().kycDocumentDao().add(kycDocumentModel)
            startDocumentWorkerTask()
            handler.post {
                Toast.makeText(this@DocumentUploadingActivity, "Document Saved Successfully", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun startDocumentWorkerTask() {
        val mWorkManager = WorkManager.getInstance()
        val mRequest = OneTimeWorkRequest.Builder(UploadDocumentWorker::class.java).build()
        mWorkManager.enqueue(mRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DOCUMENT_REQ_CODE -> if (resultCode == Activity.RESULT_OK) onDocumentSelected(data)
            else -> Toast.makeText(this@DocumentUploadingActivity, "Did not pick image, please try again!", Toast.LENGTH_LONG).show()
        }
    }
}