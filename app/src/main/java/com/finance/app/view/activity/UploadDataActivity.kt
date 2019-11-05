package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.finance.app.R
import com.finance.app.databinding.ActivityUploadDataBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.DocumentUploadConnector
import com.finance.app.presenter.presenter.DocumentUploadPresenter
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import retrofit2.http.Multipart
import javax.inject.Inject


class UploadDataActivity : BaseAppCompatActivity(), DocumentUploadConnector.ViewOpt {

    // used to bind element of layout to activity
    private val binding: ActivityUploadDataBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_upload_data)

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    private val documentPresenter = DocumentUploadPresenter(this)
    private lateinit var documentType: ArrayList<DropdownMaster>
    private var bundle: Bundle? = null

    companion object {
        private const val KEY_DOCUMENT_TYPE = "documentType"
        fun start(context: Context, dropDowns: ArrayList<DropdownMaster>) {
            val intent = Intent(context, UploadDataActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(KEY_DOCUMENT_TYPE, dropDowns)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        getDocumentType()
        hideToolbar()
        hideSecondaryToolbar()
        setClickListeners()
    }

    private fun getDocumentType() {
        documentType = ArrayList()
        bundle = intent.extras
        bundle?.let {
            documentType = bundle!!.getSerializable(KEY_DOCUMENT_TYPE) as ArrayList<DropdownMaster>
        }
        setDropDownValue(documentType)
    }

    private fun setDropDownValue(documentType: ArrayList<DropdownMaster>) {
        binding.spinnerDocumentType.adapter = MasterSpinnerAdapter(this, documentType)

    }

    private fun setClickListeners() {
        binding.btnUpload.setOnClickListener {
            documentPresenter.callNetwork(ConstantsApi.CALL_DOCUMENT_UPLOAD)
        }
    }

    override val leadId: Int?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val applicantId: Int?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val documentName: String?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val documentTypeDetailId: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val actualDocument: Multipart
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getDocumentUploadSuccess(value: Response.ResponseDocumentUpload) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDocumentUploadFailure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
