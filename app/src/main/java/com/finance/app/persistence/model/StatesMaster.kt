package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class StatesMaster : Serializable {

    @PrimaryKey
    var stateID: Int? = 0
    var stateName: String = ""
}