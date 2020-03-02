package com.finance.app.workers.Workers.document

import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.finance.app.persistence.model.KycDocumentModel
import com.finance.app.view.utils.FileUtils
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.DataBaseUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class UploadDocumentWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @Inject
    lateinit var database: DataBaseUtil
    @Inject
    lateinit var apiProject: ApiProject

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun doWork(): Result {
        val documentList = database.provideDataBaseSource().kycDocumentDao().get()
        documentList.forEach { document ->
            document?.let {
                val uploadCall = apiProject.api.postUploadDocument(prepareMultipartFile(it), prepareMultipartBody(it))
                val response = uploadCall.execute()
                if (response.isSuccessful) {
                    database.provideDataBaseSource().kycDocumentDao().delete(it.id)
                }
            }
        }

        return Result.success()
    }

    private fun prepareMultipartFile(documentModel: KycDocumentModel): MultipartBody.Part {
        val file = FileUtils.getFile(applicationContext, Uri.parse(documentModel.document))
        val fileBody = RequestBody.create(MediaType.parse(applicationContext.contentResolver.getType(Uri.parse(documentModel.document))), file)
        return MultipartBody.Part.createFormData("document", file.name, fileBody)
    }

    private fun prepareMultipartBody(documentModel: KycDocumentModel): HashMap<String, RequestBody> {
        val body = HashMap<String, RequestBody>()
        body["leadID"] = RequestBody.create(MediaType.parse("text/plain"), documentModel.leadID.toString())
        body["documentID"] = RequestBody.create(MediaType.parse("text/plain"), documentModel.documentID.toString())
        body["documentName"] = RequestBody.create(MediaType.parse("text/plain"), documentModel.documentName.toString())
        body["leadApplicantNumber"] = RequestBody.create(MediaType.parse("text/plain"), documentModel.leadApplicantNumber.toString())

        //For Testing Purpose..
//        body["leadID"] = RequestBody.create(MediaType.parse("text/plain"), "2")
//        body["documentID"] = RequestBody.create(MediaType.parse("text/plain"), "3")
//        body["documentName"] = RequestBody.create(MediaType.parse("text/plain"), "test")
//        body["leadApplicantNumber"] = RequestBody.create(MediaType.parse("text/plain"), "2003")

        return body
    }
}