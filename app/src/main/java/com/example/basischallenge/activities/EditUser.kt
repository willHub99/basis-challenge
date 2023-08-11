package com.example.basischallenge.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basischallenge.R
import com.example.basischallenge.database.model.Address
import com.example.basischallenge.database.model.User
import com.example.basischallenge.databinding.ActivityEditUserBinding
import com.example.basischallenge.recyclerview.ItemClickListener
import com.example.basischallenge.recyclerview.addressUserList.AddressAdapter
import com.example.basischallenge.utils.PhoneListener
import com.example.basischallenge.utils.cnpjListener
import com.example.basischallenge.utils.cpfListener
import com.example.basischallenge.utils.isValidEmail
import com.example.basischallenge.utils.isValidPhone
import com.example.basischallenge.viewmodels.EditUserViewModel

class EditUser : AppCompatActivity(), View.OnClickListener {

    private val binding: ActivityEditUserBinding by lazy {
        ActivityEditUserBinding.inflate(layoutInflater)
    }

    private val adapter: AddressAdapter by lazy {
        AddressAdapter()
    }
    private val viewModel: EditUserViewModel by viewModels()

    private val launchActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val typeaddress = data!!.getStringExtra("type")
            val addressAddress = data.getStringExtra("address")
            val numberAddress = data.getStringExtra("number")
            val complementAddress = data.getStringExtra("complement")
            val neighborhoodAddress = data.getStringExtra("neighborhood")
            val cepAddress = data.getStringExtra("cep")
            val cityAddress = data.getStringExtra("city")
            val ufAddress = data.getStringExtra("uf")
            val address = Address().apply {
                typeAddress = typeaddress ?: ""
                address = addressAddress ?: ""
                number = numberAddress?.let { Integer.parseInt(it) } ?: 0
                complement = complementAddress?.let { Integer.parseInt(it) } ?: 0
                neighborhood = neighborhoodAddress ?: ""
                cep = cepAddress ?: ""
                city = cityAddress ?: ""
                uf = ufAddress ?: ""
            }
            viewModel.listAddress.add(address)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureRecyclerView()
        configureItemsDropDownSelectUserType()
        val userId = intent.extras?.getString("id")
        userId?.let { name ->
            viewModel.getUser(name)
        }
        viewModel.user.observe(this@EditUser) {
            it?.let { user ->
                Log.d("edituser", "${user.address}")
                settingData(user = user)
            }
        }
        addMaskListeners()
        binding.fabAddAddressEdit.setOnClickListener(this)
        binding.btnSaveEditUser.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fabAddAddressEdit) {
            val intent = Intent(this@EditUser, CreateAddress::class.java)
            launchActivity.launch(intent)
        } else if (view.id == R.id.btnSaveEditUser) {
            updateUser()
        }
    }


    override fun onResume() {
        super.onResume()
        adapter.setAddressList(viewModel.listAddress)
    }

    private fun settingData(user: User) {
        showTextFields(user.type)
        binding.actvItemsEdit.setText(resources.getStringArray(R.array.user_types)[0], false)
        binding.tfFieldNameEdit.editText?.setText(user.name)
        binding.tfFieldSocialReasonEdit.editText?.setText(user.name)
        binding.tfFieldCpfEdit.editText?.setText(user.document)
        binding.tfFieldCnpjEdit.editText?.setText(user.document)
        binding.tfFieldPhoneEdit.editText?.setText(user.phone)
        binding.tfFieldEmailEdit.editText?.setText(user.email)
    }

    private fun showTextFields(selected: String) {
        val types = resources.getStringArray(R.array.user_types)
        if (selected == types[0]) {
            binding.tfFieldNameEdit.visibility = View.VISIBLE
            binding.tfFieldCpfEdit.visibility = View.VISIBLE
            binding.tfFieldSocialReasonEdit.visibility = View.GONE
            binding.tfFieldCnpjEdit.visibility = View.GONE
        } else if (selected == types[1]) {
            binding.tfFieldNameEdit.visibility = View.GONE
            binding.tfFieldCpfEdit.visibility = View.GONE
            binding.tfFieldSocialReasonEdit.visibility = View.VISIBLE
            binding.tfFieldCnpjEdit.visibility = View.VISIBLE
        }
    }

    private fun configureRecyclerView() {
        val recyclerView = binding.rvAddressList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setListener(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                val alert = AlertDialog.Builder(this@EditUser)
                alert.setMessage("Você tem certeza que deseja excluir esse endereço ?")
                alert.setPositiveButton("sim") { _, _ ->
                    viewModel.user.value?.let {
                        viewModel.deleteUserAddress(it._id, viewModel.listAddress[position]._id, position)
                    }
                    adapter.notifyItemRemoved(position)
                }
                alert.setNegativeButton("não") { _, _ -> }
                alert.show()
            }
        })
    }

    private fun configureItemsDropDownSelectUserType() {
        val types = resources.getStringArray(R.array.user_address_types)
        val adapterDropDown = ArrayAdapter(this, R.layout.drop_down_item, types)
        binding.actvItemsEdit.setAdapter(adapterDropDown)

        binding.actvItemsEdit.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val itemSelected = parent.getItemAtPosition(position)
                showTextFields(itemSelected.toString())
            }
    }

    private fun updateUser() {
        val messageError: String = validateForm()
        if (messageError.isNotEmpty()) {
            val alert = AlertDialog.Builder(this@EditUser)
            alert.setTitle("Campos obrigatórios")
            alert.setMessage(messageError)
            alert.setPositiveButton("ok") { _, _ -> }
            alert.show()
        } else {
            val (userName, userDocument) =
                if (binding.actvItemsEdit.text.toString() == "fisíca") Pair(binding.tfFieldNameEdit.editText?.text.toString(), binding.tfFieldCpfEdit.editText?.text.toString())
                else Pair(binding.tfFieldSocialReasonEdit.editText?.text.toString(), binding.tfFieldCnpjEdit.editText?.text.toString())
            viewModel.updateUser(
                User().apply {
                    _id = viewModel.user.value?._id.toString()
                    type = binding.actvItemsEdit.text.toString()
                    name = userName
                    document = userDocument
                    phone = binding.tfFieldPhoneEdit.editText?.text.toString()
                    email = binding.tfFieldEmailEdit.editText?.text.toString()
                    address.addAll(viewModel.listAddress)
                }
            )
            finish()
        }
    }

    private fun validateForm(): String {
        var messageError = ""
        with(binding) {
            if (actvItemsEdit.text.toString() == "fisíca") {
                if (tfFieldNameEdit.editText?.text.isNullOrEmpty()) messageError += "nome "
                if (tfFieldCpfEdit.editText?.text.isNullOrEmpty()) messageError += "cpf "
            } else if (actvItemsEdit.text.toString() == "jurídica") {
                if (tfFieldSocialReasonEdit.editText?.text.isNullOrEmpty()) messageError += "razao social "
                if (tfFieldCnpjEdit.editText?.text.isNullOrEmpty()) messageError += "cnpj "
            }
            if (tfFieldPhoneEdit.editText?.text.toString().isValidPhone()) messageError += "telefone mal formatado "
            if (tfFieldEmailEdit.editText?.text.toString().isValidEmail()) messageError += "email mal formatado "
        }
        return messageError
    }

    private fun addMaskListeners() {
        binding.tfFieldPhoneEdit.editText?.addTextChangedListener(PhoneListener)
        binding.tfFieldCpfEdit.editText?.addTextChangedListener(cpfListener)
        binding.tfFieldCnpjEdit.editText?.addTextChangedListener(cnpjListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tfFieldPhoneEdit.editText?.removeTextChangedListener(PhoneListener)
        binding.tfFieldCpfEdit.editText?.removeTextChangedListener(cpfListener)
        binding.tfFieldCnpjEdit.editText?.removeTextChangedListener(cnpjListener)
    }
}