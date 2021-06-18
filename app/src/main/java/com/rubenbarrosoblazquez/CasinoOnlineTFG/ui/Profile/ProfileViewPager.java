package com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.R;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.ProductsFragment;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.ServiceGamesFragment;
import com.rubenbarrosoblazquez.CasinoOnlineTFG.ui.Servicios.ServiciosFragment;

import java.util.ArrayList;

public class ProfileViewPager extends Fragment {

    private viewPager2Adapter adapter;
    private ArrayList<Fragment> arrayList = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_view_pager_profile, container, false);

        ViewPager2 viewPager = root.findViewById(R.id.pagerProfile);

        arrayList.add(new ServiceGamesFragment());
        arrayList.add(new ProductsFragment());

        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        adapter = new viewPager2Adapter(getActivity().getSupportFragmentManager(), getLifecycle(), root);

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
        viewPager.setPageTransformer(new MarginPageTransformer(1500));

        TabLayout tabLayout = root.findViewById(R.id.tab_layoutProfile);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:

                            tab.setText(getString(R.string.menu_tab_profile));
                            break;
                        case 1:
                            tab.setText(getString(R.string.menu_tab_basket));
                            break;
                    }
                }).attach();


        return root;
    }

    public class viewPager2Adapter extends FragmentStateAdapter {
        private View v;

        public viewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, View v) {
            super(fragmentManager, lifecycle);
            this.v = v;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ProfileFragment();
                case 1:
                    return new BasketFragment();

            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }
}
