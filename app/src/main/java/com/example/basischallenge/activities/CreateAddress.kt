package com.example.basischallenge.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.basischallenge.R
import com.example.basischallenge.databinding.ActivityCreateAddressBinding
import com.example.basischallenge.utils.cepListener
import com.example.basischallenge.utils.isUfValid

class CreateAddress : AppCompatActivity(), View.OnClickListener {

    private val binding: ActivityCreateAddressBinding by lazy {
        ActivityCreateAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureItemsDropDownSelectUserType()
        addMaskListeners()
        binding.btnSaveAddress.setOnClickListener(this)
    }

    private fun configureItemsDropDownSelectUserType() {
        val types = resources.getStringArray(R.array.user_address_types)
        val adapter = ArrayAdapter(this, R.layout.drop_down_item, types)
        binding.actvAddress.setAdapter(adapter)

        binding.actvAddress.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val itemSelected = parent.getItemAtPosition(position)
            }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnSaveAddress) {
            val errorMessage = validateForm()
            if (errorMessage.isEmpty()) {
                val intent = Intent()
                intent.putExtra("type", binding.actvAddress.text.toString())
                intent.putExtra("address", binding.tfFieldAddress.editText?.text.toString())
                intent.putExtra("number", binding.tfFieldNumber.editText?.text.toString())
                intent.putExtra("complement", binding.tfFieldComplement.editText?.text.toString())
                intent.putExtra("neighborhood", binding.tfFieldNeighborhood.editText?.text.toString())
                intent.putExtra("cep", binding.tfFieldCep.editText?.text.toString())
                intent.putExtra("city", binding.tfFieldCity.editText?.text.toString())
                intent.putExtra("uf", binding.tfFieldUf.editText?.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            } else {
                val alert = AlertDialog.Builder(this@CreateAddress)
                alert.setTitle("Campos obrigatórios")
                alert.setMessage("$errorMessage")
                alert.setPositiveButton("ok") { _, _ -> }
                alert.show()
            }
        }
    }

    private fun validateForm(): String {
        var errorMessage = ""
        with(binding) {
            if (tfFieldCep.editText?.text.isNullOrEmpty()) errorMessage += "cep "
            if (tfFieldNumber.editText?.text.isNullOrEmpty()) errorMessage += "número "
            if (tfFieldComplement.editText?.text.isNullOrEmpty()) errorMessage += "complemento "
            if (tfFieldNeighborhood.editText?.text.isNullOrEmpty()) errorMessage += "bairo "
            if (tfFieldAddress.editText?.text.isNullOrEmpty()) errorMessage += "endereco "
            if (tfFieldUf.editText?.text.toString().isUfValid()) errorMessage += "uf nao existe "
            if (tfFieldCity.editText?.text.isNullOrEmpty()) errorMessage += "cidade "
        }
        return errorMessage
    }

    private fun addMaskListeners() {
        binding.tfFieldCep.editText?.addTextChangedListener(cepListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tfFieldCep.editText?.removeTextChangedListener(cepListener)
    }
}