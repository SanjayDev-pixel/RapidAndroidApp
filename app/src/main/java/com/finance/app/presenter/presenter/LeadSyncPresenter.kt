package com.finance.app.presenter.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.LoanApplicationRequest
import com.finance.app.presenter.connector.LeadSyncConnector
import com.finance.app.utility.LeadRequestResponseConversion
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response.ResponseGetLoanApplication
import javax.inject.Inject


/**
 * Created by motobeans on 1/2/2018.
 */
class LeadSyncPresenter(
        private val viewOptLocalToServer: LeadSyncConnector.ViewOptLocalToServer? = null,
        private val viewOptServerToLocal: LeadSyncConnector.ViewOptServerToLocal? = null) : LeadSyncConnector.PresenterOpt {

  @Inject
  lateinit var apiProject: ApiProject
  @Inject
  lateinit var database: DataBaseUtil
  @Inject
  lateinit var sharedPreferencesUtil: SharedPreferencesUtil

  init {
    ArchitectureApp.instance.component.inject(this)
  }


  override fun callNetwork(type: ConstantsApi) {
    when (type) {
      ConstantsApi.CALL_SYNC_LEAD_LOCAL_TO_SERVER -> callSyncAllNotSyncLeadsToServer()
      else -> {
      }
    }
  }

  private lateinit var observableLocalToServerCheckOutOnly: Observer<List<AllLeadMaster>?>
  private lateinit var observerLocalToServerCheckOutOnly: LiveData<List<AllLeadMaster>?>

  /**
   * Check In Sync
   */
  private fun callSyncAllNotSyncLeadsToServer() {
    observableLocalToServerCheckOutOnly = Observer { items ->
      when (items?.size ?: 0 > 0) {
        true -> sendRecordsOneByOneToServer(items)
      }

      observerLocalToServerCheckOutOnly.removeObserver(observableLocalToServerCheckOutOnly)
    }

    observerLocalToServerCheckOutOnly = database.provideDataBaseSource().allLeadsDao().getAllLeadsNotSyncWithServer()
    observerLocalToServerCheckOutOnly.observeForever(observableLocalToServerCheckOutOnly)
  }

  private fun sendRecordsOneByOneToServer(items: List<AllLeadMaster>?) {
    items?.forEach {
      hitApiLeadSyncLocalToServerSeparateApiHits(itemToSync = it)
    }
  }

  private fun hitApiLeadSyncLocalToServerSeparateApiHits(itemToSync: AllLeadMaster?) {
    itemToSync?.let {
      val leadRequest = LeadRequestResponseConversion()
      val requestLoanInfo = leadRequest.getRequest(AppEnums.FormType.LOANINFO, itemToSync)
      val requestPersonal = leadRequest.getRequest(AppEnums.FormType.PERSONALINFO, itemToSync)
      val requestEmployment = leadRequest.getRequest(AppEnums.FormType.EMPLOYMENT, itemToSync)
      val requestBank = leadRequest.getRequest(AppEnums.FormType.BANKDETAIL, itemToSync)
      val requestLiabilityAndAssets = leadRequest.getRequest(AppEnums.FormType.LIABILITYASSET, itemToSync)
      val requestProperty = leadRequest.getRequest(AppEnums.FormType.PROPERTY, itemToSync)
      val requestReference = leadRequest.getRequest(AppEnums.FormType.REFERENCE, itemToSync)

      val observableLoanInfo = getObserverCommon(requestLoanInfo)
      val observablePersonal = getObserverCommon(requestPersonal)
      val observableEmployment = getObserverCommon(requestEmployment)
      val observableBank = getObserverCommon(requestBank)
      val observableLiabilityAndAssets = getObserverCommon(requestLiabilityAndAssets)
      val observableProperty = getObserverCommon(requestProperty)
      val observableReference = getObserverCommon(requestReference)

      val allObservables = listOf(observableLoanInfo, observablePersonal)

/*
      Observable.zip(allObservables) { args -> Arrays.asList(args) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({

    }, {
      val c = it
    })
    */
/*
      Observable.zip(allObservables, object : Function() {
        fun call(vararg args: Any?): ResponseGetLoanApplication? {
          var result: ReturnType //to be made
          //preparatory code for using the args
          for (obj in args) {
            val retObj: ReturnType? = obj as ReturnType?
            //code to use the arg once at a time to combine N of them into one.
          }
          return result
        }
      })*/


//      Observable.zip(allObservables, results -> ())
    }
  }

  private fun getObserverCommon(observerLoanInfo: LoanApplicationRequest?): Observable<ResponseGetLoanApplication>? {
    observerLoanInfo?.let {
      return apiProject.api.postLoanApp(observerLoanInfo)
    }

    return null
  }

  private fun hitApiLeadSyncLocalToServer(itemToSync: AllLeadMaster?) {
    itemToSync?.let {

      val requestApi = apiProject.api.postLoanAllLeadData(itemToSync)

      requestApi
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({ resposne -> onCreateOrderSyncSuccessfully(resposne.responseObj) },
                      { e ->
                        viewOptLocalToServer?.getLocalLeadSyncLocalToServerFailure(e?.message ?: "")
                      })
    }
  }

  private fun onCreateOrderSyncSuccessfully(resposne: AllLeadMaster?) {
    resposne?.let {
      GlobalScope.launch {
        database.provideDataBaseSource().allLeadsDao().insertLead(resposne)
      }

      viewOptLocalToServer?.getLocalLeadSyncLocalToServerSuccess(resposne)
    }
  }
}