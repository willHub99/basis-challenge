package com.example.basischallenge.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.basischallenge.databinding.ActivityCreateUserBinding
import com.example.basischallenge.recyclerview.ItemClickListener
import com.example.basischallenge.recyclerview.addressUserList.AddressAdapter
import com.example.basischallenge.utils.PhoneListener
import com.example.basischallenge.utils.cnpjListener
import com.example.basischallenge.utils.cpfListener
import com.example.basischallenge.utils.isValidEmail
import com.example.basischallenge.utils.isValidPhone
import com.example.basischallenge.viewmodels.CreateUserViewModel

class CreateUser : AppCompatActivity(), View.OnClickListener {

    private val binding: ActivityCreateUserBinding by lazy {
        ActivityCreateUserBinding.inflate(layoutInflater)
    }
    private val viewModel: CreateUserViewModel by viewModels()
    private val adapter: AddressAdapter by lazy {
        AddressAdapter()
    }

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
            viewModel.addressList.add(address)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureItemsDropDownSelectUserType()
        configureRecyclerView()
        binding.fabAddAddress.setOnClickListener(this)
        binding.btnSaveUser.setOnClickListener(this)
        addMaskListeners()
    }

    override fun onResume() {
        super.onResume()
        adapter.setAddressList(viewModel.addressList)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fabAddAddress) {
            val intent = Intent(this@CreateUser, CreateAddress::class.java)
            launchActivity.launch(intent)
        } else if (view.id == R.id.btnSaveUser) {
            createUser()
        }
    }

    private fun createUser() {
        val messageError: String = validateForm()
        if (messageError.isNotEmpty()) {
            val alert = AlertDialog.Builder(this@CreateUser)
            alert.setTitle("Campos obrigatórios")
            alert.setMessage(messageError)
            alert.setPositiveButton("ok") { _, _ -> }
            alert.show()
        } else {
            val (userName, userDocument) =
                if (binding.actvItems.text.toString() == "fisíca") Pair(binding.tfFieldName.editText?.text.toString(), binding.tfFieldCpf.editText?.text.toString())
                else Pair(binding.tfFieldSocialReason.editText?.text.toString(), binding.tfFieldCnpj.editText?.text.toString())
            viewModel.createUser(
                User().apply {
                    type = binding.actvItems.text.toString()
                    name = userName
                    document = userDocument
                    phone = binding.tfFieldPhone.editText?.text.toString()
                    email = binding.tfFieldEmail.editText?.text.toString()
                    address.addAll(viewModel.addressList)
                }
            )
            finish()
        }
    }

    private fun validateForm(): String {
        var messageError = ""
        with(binding) {
            if (actvItems.text.toString() == "fisíca") {
                if (tfFieldName.editText?.text.isNullOrEmpty()) messageError += "nome "
                if (tfFieldCpf.editText?.text.isNullOrEmpty()) messageError += "cpf "
            } else if (actvItems.text.toString() == "jurídica") {
                if (tfFieldSocialReason.editText?.text.isNullOrEmpty()) messageError += "razao social "
                if (tfFieldCnpj.editText?.text.isNullOrEmpty()) messageError += "cnpj "
            }
            if (tfFieldPhone.editText?.text.toString().isValidPhone()) messageError += "telefone mal formatado "
            if (tfFieldEmail.editText?.text.toString().isValidEmail()) messageError += "email mal formatado "
        }
        return messageError
    }

    private fun configureRecyclerView() {
        val recyclerView = binding.rvAddressList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setListener(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                val alert = AlertDialog.Builder(this@CreateUser)
                alert.setMessage("Você tem certeza que deseja excluir esse endereço ?")
                alert.setPositiveButton("sim") { _, _ ->
                    viewModel.addressList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
                alert.setNegativeButton("não") { _, _ -> }
                alert.show()
            }
        })
    }

    private fun configureItemsDropDownSelectUserType() {
        val types = resources.getStringArray(R.array.user_types)
        val adapter = ArrayAdapter(this, R.layout.drop_down_item, types)
        binding.actvItems.setAdapter(adapter)

        binding.actvItems.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val itemSelected = parent.getItemAtPosition(position)
                if (itemSelected == types[0]) {
                    binding.tfFieldName.visibility = View.VISIBLE
                    binding.tfFieldCpf.visibility = View.VISIBLE
                    binding.tfFieldSocialReason.visibility = View.GONE
                    binding.tfFieldCnpj.visibility = View.GONE
                } else if (itemSelected == types[1]) {
                    binding.tfFieldName.visibility = View.GONE
                    binding.tfFieldCpf.visibility = View.GONE
                    binding.tfFieldSocialReason.visibility = View.VISIBLE
                    binding.tfFieldCnpj.visibility = View.VISIBLE
                }
            }
    }

    private fun addMaskListeners() {
        binding.tfFieldPhone.editText?.addTextChangedListener(PhoneListener)
        binding.tfFieldCpf.editText?.addTextChangedListener(cpfListener)
        binding.tfFieldCnpj.editText?.addTextChangedListener(cnpjListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tfFieldPhone.editText?.removeTextChangedListener(PhoneListener)
        binding.tfFieldCpf.editText?.removeTextChangedListener(cnpjListener)
        binding.tfFieldCnpj.editText?.removeTextChangedListener(cnpjListener)
    }
}