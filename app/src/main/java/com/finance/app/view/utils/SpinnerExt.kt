package com.finance.app.view.utils

import com.finance.app.persistence.model.DropdownMaster
import fr.ganfra.materialspinner.MaterialSpinner
import java.util.*


fun MaterialSpinner.setSelectionFromList(dropDowns: ArrayList<DropdownMaster>, value: Int) {
    dropDowns.forEachIndexed { index, dropdownMaster ->
        if (dropdownMaster.typeDetailID == value) {
            this.setSelection(index + 1, true)
            return@forEachIndexed
        }
    }
}
