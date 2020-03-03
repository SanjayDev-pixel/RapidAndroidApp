package motobeans.architecture.constants

class Constants {

    object Injection {
        const val API_CURRENT_URL = "currentURL"
        const val API_DEVELOPMENT_URL = "developmentURL"
        const val API_TESTING_URL = "testingURL"
        const val API_LIVE_URL = "liveURL"
        const val API_PRODUCTION_URL = "productionURL"

        /**
         * Network Class v1 constants
         */
        const val RETROFIT_V1 = "RETROFIT_V1"
        const val GSON_V1 = "GSON_V1"
        const val ENDPOINT_V1 = "GSON_V1"
        const val OKHHTP_CACHE_V1 = "OKHTTP_CACHE_V1"
        const val OKHHTP_CLIENT_V1 = "OKHTTP_CLIENT_V1"
        const val INTERCEPTOR_HEADER_V1 = "INTERCEPTOR_HEADER_V1"
        const val INTERCEPTOR_LOGGING_V1 = "INTERCEPTOR_LOGGING_V1"
        const val INTERCEPTOR_RESPONSE_V1 = "INTERCEPTOR_RESPONSE_V1"
    }

    object API {
        object URL {
            const val URL_DEVELOPMENT = "http://13.235.28.32:8080/dmi/"
            //            const val URL_DEVELOPMENT = "http://13.232.224.66:8080/dmi/"
            const val URL_KYC = "http://13.235.28.32/kyc/kyc.html?kycID="
            const val URL_TESTING = ""
            const val URL_LIVE = ""
            const val URL_PRODUCTION = ""
            const val KYCID = ""
        }
    }

    companion object APP {
        const val SUCCESS = "200"
        const val DIRECT = 53
        const val SELF = 221
        const val RENTED = 253
        const val SINGLE = 63
        internal const val PERMANENT_ADDRESS = "Permanent"
        internal const val CURRENT_ADDRESS = "Current"
        const val KEY_APPLICANT = "applicant"
        const val KEY_CO_APPLICANT = "coApplicant"
        const val KEY_INDEX = "index"
        const val KEY_PERSONAL_APPLICANT = "personalApplicantModel"
        const val KEY_LEAD_APP_NUM = "leadApplicantNum"
        const val KEY_DOC_ID = "doc_id"
        const val KEY_APPLICANT_NUMBER = "applicant_number"
        const val KEY_APPLICATION_SCREEN = "applicationScreen"

        const val SALARY = 0
        const val SENP = 1
        const val ASSESED_INCOME = 116
        const val ITR = 117
        const val CASH_SALARY = 118
        const val BANK_SALARY = 119

    }

}