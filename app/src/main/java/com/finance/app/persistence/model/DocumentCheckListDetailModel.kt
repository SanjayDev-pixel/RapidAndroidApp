package com.finance.app.persistence.model

import androidx.room.Ignore
import java.io.Serializable

class DocumentCheckListDetailModel : Serializable {
    var checklistDetailId: Int? = null
    var checklistId: Int? = null
    var description: String? = null
    var typeDetailId: Int? = null
    var typeDetailDisplayText: String? = null
    @Ignore var selectedCheckListValue: ChecklistAnswerType? = null

}