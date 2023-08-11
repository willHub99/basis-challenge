package com.example.basischallenge.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basischallenge.databinding.ActivityMainBinding
import com.example.basischallenge.recyclerview.ItemClickListener
import com.example.basischallenge.recyclerview.userList.UserAdapter
import com.example.basischallenge.viewmodels.HomeViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adapter: UserAdapter by lazy {
        UserAdapter()
    }
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureRecyclerView()
        settingSearchView()
        binding.fabAddUser.setOnClickListener(this)
    }

    private fun settingSearchView() {
        binding.svFilter.clearFocus()
        binding.svFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    viewModel.users.value?.let { adapter.setList(it) }
                } else {
                    val filteredList = viewModel.filterUserList(newText)
                    filteredList?.let {
                        adapter.setList(it)
                    }
                }
                adapter.notifyDataSetChanged()
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsers()
        viewModel.users.observe(this) {
            it?.let {users ->
                adapter.setList(users)
            }
        }
    }

    private fun configureRecyclerView() {
        val recyclerView = binding.rvListUsers
        adapter.setList(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setListener(object : ItemClickListener{
            override fun onItemClick(position: Int) {
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setMessage("Você tem certeza que quer excluir o usuário ${viewModel.users.value?.get(position)?.name}")
                alert.setPositiveButton("sim") { _, _ ->
                    val user = viewModel.users.value?.get(position)
                    user?.let {
                        viewModel.deleteUser(it._id)
                        adapter.notifyItemRemoved(position)
                    }
                }
                alert.setNegativeButton("não") {_, _ ->}
                alert.show()
            }
        })
    }

    override fun onClick(view: View) {
        if (view.id == binding.fabAddUser.id) {
            Intent(this, CreateUser::class.java).also {
                startActivity(it)
           }
        }
    }

}