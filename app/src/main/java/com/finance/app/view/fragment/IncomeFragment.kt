package com.finance.app.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.finance.app.R
import com.finance.app.databinding.FragmentIncomeBinding
import com.finance.app.databinding.FragmentLoanInformationBinding

class IncomeFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentIncomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showTableLayout()
    }

    private fun showTableLayout() {
        val tbrow0 = TableRow(context)
        val tv0 = TextView(context)
        tv0.text = " Sl.No "
        tv0.setTextColor(Color.BLACK)
        tbrow0.addView(tv0)
        val tv1 = TextView(context)
        tv1.text = " Product "
        tv1.setTextColor(Color.BLACK)
        tbrow0.addView(tv1)
        val tv2 = TextView(context)
        tv2.text = " Unit Price "
        tv2.setTextColor(Color.BLACK)
        tbrow0.addView(tv2)
        val tv3 = TextView(context)
        tv3.text = " Stock Remaining "
        tv3.setTextColor(Color.BLACK)
        tbrow0.addView(tv3)
        binding.tableFinancialYear.addView(tbrow0)
        for (i in 0..24) {
            val tbrow = TableRow(context)
            val t1v = TextView(context)
            t1v.text = "" + i
            t1v.setTextColor(Color.BLACK)
            t1v.gravity = Gravity.CENTER
            tbrow.addView(t1v)
            val t2v = TextView(context)
            t2v.text = "Product $i"
            t2v.setTextColor(Color.BLACK)
            t2v.gravity = Gravity.CENTER
            tbrow.addView(t2v)
            val t3v = TextView(context)
            t3v.text = "Rs.$i"
            t3v.setTextColor(Color.BLACK)
            t3v.gravity = Gravity.CENTER
            tbrow.addView(t3v)
            val t4v = TextView(context)
            t4v.text = "" + i * 15 / 32 * 10
            t4v.setTextColor(Color.BLACK)
            t4v.gravity = Gravity.CENTER
            tbrow.addView(t4v)
            binding.tableFinancialYear.addView(tbrow)
        }
    }
}