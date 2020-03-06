package com.finance.app.persistence.model

import java.io.Serializable

class DocumentCheckList : Serializable {
    var productID : Int ? =null
    var productName : String ? = null
    var checklistDetails : ArrayList<DocumentCheckListDetailModel> ? = null
}