package com.finance.app.persistence.model

import java.util.*

class DropdownMaster {

    var localId = UUID.randomUUID().toString()
    var accommodation_id: String = ""
    var hotel_address: String? = null
    var no_of_rooms: String? = null
    var no_of_trainers: String? = null
    var training_request_id: String? = null
}