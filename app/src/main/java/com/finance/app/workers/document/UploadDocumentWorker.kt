package com.finance.app.workers.document

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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


class UploadDocumentWorker(context: Context , workerParams: WorkerParameters) : Worker(context , workerParams) {
    @Inject
    lateinit var database: DataBaseUtil
    @Inject
    lateinit var apiProject: ApiProject

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun doWork(): Result {
        val documentList = database.provideDataBaseSource().kycDocumentDao().get()
        System.out.println("Size>>>>"+documentList.size)

        //get Input Data back using "inputData" variable
        documentList.forEach { document ->
            document?.let {
                val uploadCall = apiProject.api.postUploadDocument(prepareMultipartFile(it) , prepareMultipartBody(it))
                val response = uploadCall.execute()
                if (response.isSuccessful) {
                   /* val leadApplicantNumber = response.body()?.responseObj?.leadApplicantNumber
                    System.out.println("Response Object>>>"+response.body()?.responseObj?.leadApplicantNumber)
                    System.out.println("Response Object>>>"+response.body()?.responseObj?.documentID)*/
                    database.provideDataBaseSource().kycDocumentDao().delete(it.id)
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        // Run your task here
                        Toast.makeText(applicationContext , "Your document has been uploaded successfully" , Toast.LENGTH_SHORT).show()
                    } , 1000)
                    //Toast.makeText(applicationContext,"Your document has been submitted successfully",Toast.LENGTH_LONG).show()

                }
            }
        }

        return Result.success()
    }

    private fun prepareMultipartFile(documentModel: KycDocumentModel): MultipartBody.Part {
        val file = FileUtils.getFile(applicationContext , Uri.parse(documentModel.document))
        val fileBody = RequestBody.create(MediaType.parse(applicationContext.contentResolver.getType(Uri.parse(documentModel.document))) , file)
        return MultipartBody.Part.createFormData("document" , file.name , fileBody)
    }

    private fun prepareMultipartBody(documentModel: KycDocumentModel): HashMap<String , RequestBody> {
        var splitLeadId : String = ""
        if(documentModel.leadID == null)
        {
            splitLeadId = documentModel.leadApplicantNumber!!.split("_".toRegex())[0]
            System.out.println("leadId>>>If>>>>"+splitLeadId)
        }
        else
        {
            splitLeadId = documentModel.leadID.toString()
            System.out.println("leadId>>>else>>>>"+splitLeadId)
        }
        val body = HashMap<String , RequestBody>()
        body["leadID"] = RequestBody.create(MediaType.parse("text/plain") , splitLeadId.trim())
        //body["formId"] = if (documentModel.formId.isNullOrEmpty().not()) RequestBody.create(MediaType.parse("text/plain") , documentModel.formId.toString()) else RequestBody.create(MediaType.parse("text/plain") , "")
        //body["rowIdentifier"] = if (documentModel.applicationDocumentID.isNullOrEmpty().not()) RequestBody.create(MediaType.parse("text/plain") , documentModel.applicationDocumentID.toString()) else RequestBody.create(MediaType.parse("text/plain") , "")
        body["documentID"] = RequestBody.create(MediaType.parse("text/plain") , documentModel.documentID.toString())
        body["documentName"] = RequestBody.create(MediaType.parse("text/plain") , documentModel.documentName.toString())
        body["leadApplicantNumber"] = RequestBody.create(MediaType.parse("text/plain") , documentModel.leadApplicantNumber.toString())

        return body
    }
}