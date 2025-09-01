package com.example.mvvm_study

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_lib.routers.ModuleRoutes
import com.example.mvvm_study.ui.EssayFragment
import com.example.mvvm_study.ui.SNH48Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

@Route(path = ModuleRoutes.MODULE_MVVM_HOME)
class MvvmMainActivity : AppCompatActivity() {

    private lateinit var fragment1: EssayFragment
    private lateinit var fragment3: EssayFragment
    private lateinit var fragment2: SNH48Fragment

    private lateinit var fragments: Array<Fragment>

    private var lastShowFragment = 0

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (lastShowFragment != 0) {
                        switchFragment(lastShowFragment, 0)
                        lastShowFragment = 0
                    }
                    true
                }

                R.id.navigation_dashboard -> {
                    if (lastShowFragment != 1) {
                        switchFragment(lastShowFragment, 1)
                        lastShowFragment = 1
                    }
                    true
                }

                R.id.navigation_notifications -> {
                    if (lastShowFragment != 2) {
                        switchFragment(lastShowFragment, 2)
                        lastShowFragment = 2
                    }
                    true
                }

                else -> {
                    false
                }
            }

        }

    private fun switchFragment(lastShowFragment: Int, index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(fragments[lastShowFragment])
        if (!fragments[index].isAdded) {
            transaction.add(R.id.fragment_container, fragments[index])
        }

        transaction.show(fragments[index])
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mvvm_main)
        initFragments()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mvvm_bottomnavigationview)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun initFragments() {
        fragment1 = EssayFragment()
        fragment2 = SNH48Fragment()
        fragment3 = EssayFragment()
        fragments = arrayOf(fragment1, fragment2, fragment3)

        lastShowFragment = 0

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragments[lastShowFragment])
            .show(fragments[lastShowFragment])
            .commit()
    }
}