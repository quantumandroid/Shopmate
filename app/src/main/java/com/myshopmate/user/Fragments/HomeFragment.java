package com.myshopmate.user.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myshopmate.user.Activity.AddressLocationActivity;
import com.myshopmate.user.Activity.AllStoresActivity;
import com.myshopmate.user.Activity.CategoryPage;
import com.myshopmate.user.Activity.DealActivity;
import com.myshopmate.user.Activity.Splash;
import com.myshopmate.user.Activity.ViewAll_TopDeals;
import com.myshopmate.user.Adapters.BannerAdapter;
import com.myshopmate.user.Adapters.CategoryGridAdapter;
import com.myshopmate.user.Adapters.DealsAdapter;
import com.myshopmate.user.Adapters.HomeAdapter;
import com.myshopmate.user.Adapters.HomeCategoryAdapter;
import com.myshopmate.user.Adapters.Home_adapter;
import com.myshopmate.user.Adapters.MainScreenAdapter;
import com.myshopmate.user.Adapters.PageAdapter;
import com.myshopmate.user.Adapters.PopularCatsAdapter;
import com.myshopmate.user.Adapters.StoreProductsPagerAdapter;
import com.myshopmate.user.Categorygridquantity;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.Constans.RecyclerTouchListener;
import com.myshopmate.user.ModelClass.Category_model;
import com.myshopmate.user.ModelClass.HomeCate;
import com.myshopmate.user.ModelClass.MainScreenList;
import com.myshopmate.user.ModelClass.NewCartModel;
import com.myshopmate.user.ModelClass.NewCategoryDataModel;
import com.myshopmate.user.ModelClass.NewCategoryVarientList;
import com.myshopmate.user.ModelClass.PopularCategoryModel;
import com.myshopmate.user.ModelClass.PopularCategoryResponse;
import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.R;
import com.myshopmate.user.network.ApiInterface;
import com.myshopmate.user.util.AppController;
import com.myshopmate.user.util.CustomVolleyJsonRequest;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.FragmentClickListner;
import com.myshopmate.user.util.Session_management;
import com.myshopmate.user.util.Utils;
import com.volley.simple_request.OnResponseListener;
import com.volley.simple_request.SimpleRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.myshopmate.user.Config.BaseURL.ADDRESS;
import static com.myshopmate.user.Config.BaseURL.BANNER_IMG_URL;
import static com.myshopmate.user.Config.BaseURL.CITY;
import static com.myshopmate.user.Config.BaseURL.HomeDeal;
import static com.myshopmate.user.Config.BaseURL.HomeRecent;
import static com.myshopmate.user.Config.BaseURL.HomeTopSelling;
import static com.myshopmate.user.Config.BaseURL.LAT;
import static com.myshopmate.user.Config.BaseURL.LONG;
import static com.myshopmate.user.Config.BaseURL.MyPrefreance;
import static com.myshopmate.user.Config.BaseURL.secondary_banner;
import static com.myshopmate.user.Config.BaseURL.whatsnew;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG1 = "MainActivity";
    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;
    TabItem tab1, tab2, tab3, tab4;
    Float translationY = 100f;
    FloatingActionButton fabMain, fabOne, fabTwo, fabThree, fabfour;
    LinearLayout parent_lay;
    // CardView Search_layout;
    //    ScrollView scrollView;
    //NestedScrollView scrollView;
    RecyclerView rv_items, rv_stores;
    SliderLayout banner_slider, featuredslider;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;
    RecyclerView recyclerViewCate, recyclerViewDEal;
    HomeCategoryAdapter cateListAdapter;
    DealsAdapter dealAdapter;
    String productId;
    TextView viewall_topdeals;
    List<Address> addresses = new ArrayList<>();
    String latitude, longitude, address, city, state, country, postalCode;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;
    ArrayList<String> imageString = new ArrayList<>();
    private RecyclerView recyclerImages;
    private List<HomeCate> cateList = new ArrayList<>();
    //    private List<CategoryGrid> dealList = new ArrayList<>();
    private List<Category_model> category_modelList = new ArrayList<>();
    private List<Store> store_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private HomeAdapter adapter1;
    private boolean isSubcat = false;
    private BannerAdapter bannerAdapter, bannerAdapter1;
    private LinearLayout viewpager_layout;
    private LinearLayout change_loc_lay;
    private TextView not_product;
    private TextView change_loc;
    private ArrayList<String> imageString1 = new ArrayList<>();
    private RecyclerView recyclerImages1;
    private Session_management session_management;
    private FragmentClickListner fragmentClickListner;
    private ViewPager2 viewPager2;
    private ProgressDialog progressDialog;

    private List<MainScreenList> screenLists = new ArrayList<>();
    private List<NewCartModel> topSelling = new ArrayList<>();
    private List<NewCartModel> whatsNew = new ArrayList<>();
    private List<NewCartModel> recentSelling = new ArrayList<>();
    private List<NewCartModel> dealOftheday = new ArrayList<>();
    private MainScreenAdapter screenAdapter;
    private Context context;
    private int tabCounter = 0;

    private ViewPager viewPagerStoresProducts;
    private StoreProductsPagerAdapter storeProductsPagerAdapter;
    private RecyclerView rvProducts;
    private EditText etSearch;
    private TabLayout stores_products_tab_layout;
    private BottomNavigationView bottomNavigationView;

    List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    CategoryGridAdapter categoryGridAdapter;
    List<NewCategoryVarientList> varientProducts = new ArrayList<>();
    DatabaseHandler dbcart;

    ImageView iv_search_clear, iv_search;
    private RecyclerView rv_home_cat_products;
    private ArrayList<PopularCategoryModel> popularCategoryModels;
    private PopularCatsAdapter popularCatsAdapter;

    private LinearLayout layoutAll, layoutSearch;
    private Button cancelSearch;

    private boolean isSearchOpen = false;

    private TextView tv_all_categories,tv_all_stores;

    public HomeFragment(FragmentClickListner fragmentClickListner, BottomNavigationView bottomNavigationView) {
        this.fragmentClickListner = fragmentClickListner;
        this.bottomNavigationView = bottomNavigationView;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        requireActivity().setTitle(getResources().getString(R.string.app_name));
        context = container.getContext();
        sharedPreferences = requireContext().getSharedPreferences(MyPrefreance, MODE_PRIVATE);
        session_management = new Session_management(container.getContext());
        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Please wait while loading..");
        progressDialog.setCancelable(false);
        latitude = sharedPreferences.getString(LAT, null);
        longitude = sharedPreferences.getString(LONG, null);
        address = sharedPreferences.getString(ADDRESS, null);
        city = sharedPreferences.getString(CITY, null);
        bannerAdapter = new BannerAdapter(getActivity(), imageString);
        viewPagerStoresProducts = view.findViewById(R.id.viewPagerStoresProducts);
        etSearch = view.findViewById(R.id.et_search);
        stores_products_tab_layout = view.findViewById(R.id.stores_products_tab_layout);
        iv_search_clear = view.findViewById(R.id.iv_search_clear);
        iv_search = view.findViewById(R.id.iv_search);
        rv_home_cat_products = view.findViewById(R.id.rv_home_cat_products);
        layoutSearch = view.findViewById(R.id.layout_search);
        layoutAll = view.findViewById(R.id.layout_all);
        cancelSearch = view.findViewById(R.id.cancel_search);
        tv_all_categories = view.findViewById(R.id.tv_all_categories);
        tv_all_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CategoryFragment());
            }
        });
        tv_all_stores = view.findViewById(R.id.tv_all_stores);
        tv_all_stores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AllStoresActivity.class));
            }
        });

        // loc.setText(address+", "+city+", "+postalCode);
//        rv_items = view.findViewById(R.id.rv_home);

        rvProducts = view.findViewById(R.id.rv_home_products);
        change_loc_lay = view.findViewById(R.id.change_loc_lay);
        viewpager_layout = view.findViewById(R.id.viewpager_layout);
        not_product = view.findViewById(R.id.not_product);
        change_loc = view.findViewById(R.id.change_loc);
//        tab1 = view.findViewById(R.id.top_selling_item);
//        tab2 = view.findViewById(R.id.recent_item);
//        tab3 = view.findViewById(R.id.deals_item);
//        tab4 = view.findViewById(R.id.whtsnewitem);

        tabLayout = view.findViewById(R.id.tablayout);
        viewall_topdeals = view.findViewById(R.id.viewall_topdeals);
        viewPager = view.findViewById(R.id.pager_product);
        viewPager2 = view.findViewById(R.id.viewpa_2);
        recyclerImages1 = view.findViewById(R.id.recycler_image_slider1);
        recyclerImages = view.findViewById(R.id.recycler_image_slider);
        banner_slider = view.findViewById(R.id.relative_banner);
        featuredslider = view.findViewById(R.id.featured_img_slider);
        rv_items = view.findViewById(R.id.rv_home);
        rv_stores = view.findViewById(R.id.rv_stores);
        fabMain = view.findViewById(R.id.fabMain);
        fabOne = view.findViewById(R.id.fabOne);
        fabTwo = view.findViewById(R.id.fabTwo);
        fabThree = view.findViewById(R.id.fabThree);
        fabfour = view.findViewById(R.id.fabfour);
        parent_lay = view.findViewById(R.id.parent_lay);
        screenAdapter = new MainScreenAdapter(container.getContext(), screenLists);
        viewPager2.setAdapter(screenAdapter);
        recyclerImages.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerImages.setAdapter(bannerAdapter);
        // home_list.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));


        stores_products_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerStoresProducts.setCurrentItem(stores_products_tab_layout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupViewPager();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_items.setLayoutManager(linearLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);
//        rv_items.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*String getid = category_modelList.get(position).getCat_id();
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", getid);
                intent.putExtra("title", category_modelList.get(position).getTitle());
                intent.putExtra("image", category_modelList.get(position).getImage());
                startActivityForResult(intent, 24);
*/

                // String getid = store_modelList.get(position).getStore_id();
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", "47");
                intent.putExtra("title", store_modelList.get(position).getStore_name());
                intent.putExtra("store_id", store_modelList.get(position).getStore_id());
                intent.putExtra("is_from_category", false);
               // intent.putExtra("store_id", adapter1.getModelList().get(position).getStore_id());
               // intent.putExtra("title", adapter1.getModelList().get(position).getStore_name());
//                  intent.putExtra("image", store_modelList.get(position).getStore_image_url());
                startActivityForResult(intent, 24);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        store_modelList = new ArrayList<>();
        adapter1 = new HomeAdapter(store_modelList, getActivity(),R.layout.row_home_rv1);
        rv_items.setAdapter(adapter1);

        LinearLayoutManager layoutManagerProducts = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvProducts.setLayoutManager(layoutManagerProducts);
        rvProducts.setItemAnimator(new DefaultItemAnimator());
        rvProducts.setNestedScrollingEnabled(false);

        change_loc.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), AddressLocationActivity.class), 22));


        bannerAdapter1 = new BannerAdapter(getActivity(), imageString1);
        recyclerImages1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerImages1.setAdapter(bannerAdapter1);
//        loc=view.findViewById(R.id.loc);
        // Search_layout = view.findViewById(R.id.ll3);
        /*scrollView = view.findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);*/


        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openSearch();
                }
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && !etSearch.getText().toString().trim().isEmpty()) {
                    if (viewPagerStoresProducts.getCurrentItem() == 1) {
                        product("", etSearch.getText().toString().trim());
                    } else {
                        selectStores(etSearch.getText().toString().trim());
                    }
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    iv_search_clear.setVisibility(View.INVISIBLE);
                    iv_search.setVisibility(View.INVISIBLE);
                    if (viewPagerStoresProducts.getCurrentItem() == 1) {
                        product("", "");
                    } else {
                        selectStores("");
                    }
                } else {
                    iv_search_clear.setVisibility(View.VISIBLE);
                    iv_search.setVisibility(View.VISIBLE);
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });


        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabfour.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabfour.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabOne.setOnClickListener(this);
        fabTwo.setOnClickListener(this);
        fabThree.setOnClickListener(this);
        fabfour.setOnClickListener(this);
        iv_search_clear.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        closeMenu(false);

        setTabs();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i("TAG Demo", String.valueOf(position));
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if (position == 0) {
//                    if (topSelling.size() > 0) {
//                        viewall_topdeals.setVisibility(View.VISIBLE);
//                    } else {
//                        viewall_topdeals.setVisibility(View.GONE);
//                    }
//                } else if (position == 1) {
//                    if (recentSelling.size() > 0) {
//                        viewall_topdeals.setVisibility(View.VISIBLE);
//                    } else {
//                        viewall_topdeals.setVisibility(View.GONE);
//                    }
//                } else if (position == 2) {
//                    if (dealOftheday.size() > 0) {
//                        viewall_topdeals.setVisibility(View.VISIBLE);
//                    } else {
//                        viewall_topdeals.setVisibility(View.GONE);
//                    }
//                } else if (position == 3) {
//                    if (whatsNew.size() > 0) {
//                        viewall_topdeals.setVisibility(View.VISIBLE);
//                    } else {
//                        viewall_topdeals.setVisibility(View.GONE);
//                    }
//                }
                viewPager2.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                try {
                    if (ViewPager2.SCROLL_STATE_IDLE == state) {
                        screenAdapter.notifyItemChanged(viewPager2.getCurrentItem());
                    } else {
                        super.onPageScrollStateChanged(state);
                    }
                } catch (IllegalStateException e) {
                    super.onPageScrollStateChanged(state);
                }
            }
        });

        viewall_topdeals.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ViewAll_TopDeals.class);
            if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("TOP SELLING")) {
                if (topSelling.size() > 0) {
                    intent.putExtra("action_name", "Top_Deals_Fragment");
                    startActivityForResult(intent, 56);
                } else {
                    Toast.makeText(context, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }
            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("RECENT SELLING")) {
                if (recentSelling.size() > 0) {
                    intent.putExtra("action_name", "Recent_Details_Fragment");
                    startActivityForResult(intent, 56);
                } else {
                    Toast.makeText(context, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }

            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("DEALS OF THE DAY")) {
                if (dealOftheday.size() > 0) {
                    Intent intent1 = new Intent(v.getContext(), DealActivity.class);
                    intent1.putExtra("action_name", "Deals_Fragment");
                    startActivityForResult(intent1, 56);
                } else {
                    Toast.makeText(context, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }

            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("WHAT'S NEW")) {
                if (whatsNew.size() > 0) {
                    intent.putExtra("action_name", "Whats_New_Fragment");
                    startActivityForResult(intent, 56);
                } else {
                    Toast.makeText(context, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        dbcart = new DatabaseHandler(context);

        Categorygridquantity categorygridquantity = new Categorygridquantity() {
            @Override
            public void onClick(View view, int position, String ccId, String id) {
                varientProducts.clear();
                // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // TextView txt = view.findViewById(R.id.txt);
                // txt.setText(id);
                // LinearLayout cancl = view.findViewById(R.id.cancl);
                //  cancl.setOnClickListener(v -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
                /*RecyclerView recyler_popup = view.findViewById(R.id.recyclerVarient);
                recyler_popup.setLayoutManager(new LinearLayoutManager(context));*/
                varientProducts.addAll(newCategoryDataModel.get(position).getVarients());
                /*Adapter_popup selectCityAdapter = new Adapter_popup(context, varientProducts, id, position1 -> {
                    if (varientProducts.get(position1).getVarient_id().equalsIgnoreCase(newCategoryDataModel.get(position).getVarient_id())) {
                        categoryGridAdapter.notifyItemChanged(position);
                    }
                }, null, null, null);
                recyler_popup.setAdapter(selectCityAdapter);*/

//                Varient_product(ccId, recyler_popup, id);

            }

            @Override
            public void onCartItemAddOrMinus() {
                /*if (dbcart.getCartCount() > 0) {
                    bottom_lay_total.setVisibility(View.VISIBLE);
                    total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
                    total_count.setText("Total Items " + dbcart.getCartCount());
                } else {
                    bottom_lay_total.setVisibility(View.GONE);
                }*/
            }
        };


        // rvProducts.setLayoutManager(new GridLayoutManager(context, 1));
        rvProducts.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        categoryGridAdapter = new CategoryGridAdapter(newCategoryDataModel, context, categorygridquantity);
        rvProducts.setAdapter(categoryGridAdapter);


        popularCategoryModels = new ArrayList<>();
        popularCatsAdapter = new PopularCatsAdapter(context,popularCategoryModels);
        rv_home_cat_products.setAdapter(popularCatsAdapter);

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                setUpTabClick();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        return view;
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentPanel, fragment)
                    .commitAllowingStateLoss();
        }
    }

    private void setUpPopularStores(ArrayList<Store> stores) {
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_stores.setLayoutManager(linearLayoutManager);*/
        rv_stores.setItemAnimator(new DefaultItemAnimator());
        rv_stores.setNestedScrollingEnabled(false);
//        rv_stores.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv_stores.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_stores, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*String getid = category_modelList.get(position).getCat_id();
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", getid);
                intent.putExtra("title", category_modelList.get(position).getTitle());
                intent.putExtra("image", category_modelList.get(position).getImage());
                startActivityForResult(intent, 24);
*/

                // String getid = store_modelList.get(position).getStore_id();
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", "47");
                intent.putExtra("title", stores.get(position).getStore_name());
                intent.putExtra("store_id", stores.get(position).getStore_id());
                intent.putExtra("is_from_category", false);
                // intent.putExtra("store_id", adapter1.getModelList().get(position).getStore_id());
                // intent.putExtra("title", adapter1.getModelList().get(position).getStore_name());
//                  intent.putExtra("image", store_modelList.get(position).getStore_image_url());
                startActivityForResult(intent, 24);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

//        List<Store> store_modelList = new ArrayList<>();
        HomeAdapter adapter1 = new HomeAdapter(stores, getActivity(), R.layout.row_home_popular_stores);
        rv_stores.setAdapter(adapter1);
    }

    public void closeSearch() {
        if (isSearchOpen) {
            isSearchOpen = false;
            layoutSearch.setVisibility(View.GONE);
            layoutAll.setVisibility(View.VISIBLE);
            Utils.hideKeyboard(getActivity());
            etSearch.clearFocus();
            cancelSearch.setVisibility(View.GONE);
        }
    }

    private void openSearch() {
        if (!isSearchOpen) {
            isSearchOpen = true;
            layoutAll.setVisibility(View.GONE);
            layoutSearch.setVisibility(View.VISIBLE);
            cancelSearch.setVisibility(View.VISIBLE);
        }
    }

    public void setUpPopularCategories() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);
        apiInterface.getPopularCategoryProducts().enqueue(new Callback<PopularCategoryResponse>() {
            @Override
            public void onResponse(Call<PopularCategoryResponse> call, retrofit2.Response<PopularCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null) {
                        popularCategoryModels.clear();
                        popularCategoryModels.addAll(response.body().getData());
                        popularCatsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularCategoryResponse> call, Throwable t) {
                Log.e(TAG1,t.getMessage());
            }
        });
    }

    private void product(String store_id, String search) {
        newCategoryDataModel.clear();
        categoryGridAdapter.notifyDataSetChanged();
        if (!isUserInDelRange()) {
            Toast.makeText(context, "Delivery is not available for your location", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("search_key", search);
        params.put("is_from_category", "false");
        params.put("cat_id", "");
        /*params.put("user_lat", session_management.getLatPref());
        params.put("user_lng", session_management.getLangPref());
        params.put("centre_lat", Splash.configData.getCentre_lat());
        params.put("centre_lng", Splash.configData.getCentre_lng());
        params.put("delivery_range", Splash.configData.getDelivery_range());*/
        /*params.put("store_id", store_id);
        params.put("cat_id", cat_id);
        params.put("lat", session_management.getLatPref());
        params.put("lng", session_management.getLangPref());
        params.put("city", session_management.getLocationCity());*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.cat_product_all, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String status = response.getString("status");

//                    String message = response.getString("message");

                    if (status.contains("1")) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewCategoryDataModel>>() {
                        }.getType();
                        List<NewCategoryDataModel> listorl = gson.fromJson(response.getString("data"), listType);
                        for (NewCategoryDataModel categoryDataModel : listorl) {

                            if (categoryDataModel.getIn_stock().equals("1")){

                                newCategoryDataModel.add(categoryDataModel);
                            }
                        }
//                        newCategoryDataModel.addAll(listorl);

//                        for (int i = 0; i < listorl.size(); i++) {
//                            List<NewCategoryVarientList> listddd = listorl.get(i).getVarients();
//                            for (int j = 0; j < listddd.size(); j++) {
//                                NewCategoryShowList newCategoryShowList = new NewCategoryShowList(listorl.get(i).getProduct_id(), listorl.get(i).getProduct_name(), listorl.get(i).getProduct_image(), listddd.get(j));
//                                newModelList.add(newCategoryShowList);
//                            }
//                        }

                        categoryGridAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                newCategoryDataModel.clear();
                categoryGridAdapter.notifyDataSetChanged();
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void setSearch() {
        etSearch.requestFocus();
    }

    private void popularStores() {
        //store_modelList.clear();
        //adapter1.notifyDataSetChanged();
        if (!isUserInDelRange()) {
            Toast.makeText(context, "Delivery is not available for your location", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String sql = "select * from store where is_popular='1'" ;
        String sql = "select * from store" ;
        //String sql = "select * from store";
        SimpleRequest simpleRequest = new SimpleRequest(context);
        simpleRequest.get(sql, new OnResponseListener() {
            @Override
            public void onSuccess(com.volley.response.Response response) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    setUpPopularStores(getStores(response.getString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    // showAlert(e.getMessage());
                }


            }

            @Override
            public void onFailure(String error) {
                store_modelList.clear();
                adapter1.notifyDataSetChanged();
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void selectStores(String search_key) {
        //store_modelList.clear();
        //adapter1.notifyDataSetChanged();
        if (!isUserInDelRange()) {
            Toast.makeText(context, "Delivery is not available for your location", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select * from store where store_name like '%" + search_key + "%'";
        //String sql = "select * from store";
        SimpleRequest simpleRequest = new SimpleRequest(context);
        simpleRequest.get(sql, new OnResponseListener() {
            @Override
            public void onSuccess(com.volley.response.Response response) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    store_modelList.clear();
                    //store_modelList = getStores(response.getString());
                    store_modelList.addAll(getStores(response.getString()));
                    for (Store store : store_modelList) {
                        Utils.stores.put(store.getStore_id(), store);
                    }
                   /*adapter1 = new HomeAdapter(store_modelList, getActivity());
                    rv_items.setAdapter(adapter1);*/
                   // adapter1.setSearch(etSearch);
                    adapter1.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    // showAlert(e.getMessage());
                }


            }

            @Override
            public void onFailure(String error) {
                store_modelList.clear();
                adapter1.notifyDataSetChanged();
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setupViewPager() {
        viewPagerStoresProducts.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stores_products_tab_layout.getTabAt(position).select();
                etSearch.setText("");
                if (position == 1) {
                    etSearch.setHint("What are you looking for?");
                    Utils.hideKeyboard(getActivity());
                    etSearch.clearFocus();
                } else {
                    etSearch.setHint("Find stores");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ArrayList<Integer> layouts = new ArrayList<>();
        layouts.add(R.id.rv_home);
        layouts.add(R.id.rv_home_products);
        storeProductsPagerAdapter = new StoreProductsPagerAdapter(context, layouts);
        viewPagerStoresProducts.setAdapter(storeProductsPagerAdapter);
    }

    public ArrayList<Store> getStores(String JSON_STRING) {

        ArrayList<Store> arrayList = new ArrayList();

        try {
            JSONObject main_object = new JSONObject(JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);

                Gson gson = new Gson();
                Store store = gson.fromJson(jo.toString(), Store.class);
                arrayList.add(store);
                /*if (isUserInDelRange()) {
                    arrayList.add(store);
                }*/

            }
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return arrayList;
    }

    private boolean isUserInDelRange() {
        try {
            // DistanceCalculator distanceCalculator = new DistanceCalculator();
            //double delRange = Double.valueOf(store.getDel_range());
            double delRange = Double.parseDouble(Splash.configData.getDelivery_range());

        /*double lat1 = Double.parseDouble(store.getLat());
        double lng1 = Double.parseDouble(store.getLng());*/
            double lat1 = Double.parseDouble(Splash.configData.getCentre_lat());
            double lng1 = Double.parseDouble(Splash.configData.getCentre_lng());
            double lat2 = Double.parseDouble(session_management.getLatPref());
            double lng2 = Double.parseDouble(session_management.getLangPref());

            //double distance = distanceCalculator.distance(lat1, lng1, lat2, lng2);
            double distance = Utils.calculateMapDistance(lat1, lng1, lat2, lng2);
            // distance = distance * 2;
            // Toast.makeText(contexts, "Distance : " + distance, Toast.LENGTH_LONG).show();

            return distance <= delRange;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private void setTabs() {
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {


            if (position == 0) {
//                Log.i("TAG NEW",""+position);
                if (screenLists.size() > 0) {
                    tab.setText(screenLists.get(0).getViewType());
                }

//                if (screenLists.size() == 4){
//                    tab.setText("TOP SELLING");
//                }else {
//                    if (topSelling.size()>0){
//                        tab.setText("TOP SELLING");
//                    }else if (recentSelling.size()>0){
//                        tab.setText("RECENT SELLING");
//                    }else if (dealOftheday.size()>0){
//                        tab.setText("DEALS OF THE DAY");
//                    }else if (whatsNew.size()>0){
//                        tab.setText("WHAT'S NEW");
//                    }
//                }

            } else if (position == 1) {
//                Log.i("TAG NEW",""+position);
//                if (screenLists.size() == 4){
//                    tab.setText("RECENT SELLING");
//                }else {
//                    if (topSelling.size()>0){
//                        tab.setText("TOP SELLING");
//                    }else if (recentSelling.size()>0){
//                        tab.setText("RECENT SELLING");
//                    }else if (dealOftheday.size()>0){
//                        tab.setText("DEALS OF THE DAY");
//                    }else if (whatsNew.size()>0){
//                        tab.setText("WHAT'S NEW");
//                    }
//                }

                if (screenLists.size() > 0) {
                    tab.setText(screenLists.get(1).getViewType());
                }

            } else if (position == 2) {
//                Log.i("TAG NEW",""+position);
//                if (screenLists.size() == 4){
//                    tab.setText("DEALS OF THE DAY");
//                }else {
//                    if (topSelling.size()>0){
//                        tab.setText("TOP SELLING");
//                    }else if (recentSelling.size()>0){
//                        tab.setText("RECENT SELLING");
//                    }else if (dealOftheday.size()>0){
//                        tab.setText("DEALS OF THE DAY");
//                    }else if (whatsNew.size()>0){
//                        tab.setText("WHAT'S NEW");
//                    }
//                }
                if (screenLists.size() > 0) {
                    tab.setText(screenLists.get(2).getViewType());
                }
            } else if (position == 3) {
//                Log.i("TAG NEW",""+position);
                tab.setText("WHAT'S NEW");
            }

        });
        tabLayoutMediator.attach();
    }

    private void topSelling() {
        progressDialog.show();
        screenLists.clear();
        topSelling.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeTopSelling, response -> {
            Log.d("HomeTopSelling", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCartModel>>() {
                    }.getType();
                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    topSelling.addAll(listorl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                recentDeal();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recentDeal();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", session_management.getLatPref());
                params.put("lng", session_management.getLangPref());
                params.put("city", session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void whatsNew() {
        whatsNew.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, whatsnew, response -> {
            Log.d("HomeTopSelling", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    screenLists.add(new MainScreenList("WHAT'S NEW", topSelling, recentSelling, dealOftheday, whatsNew));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCartModel>>() {
                    }.getType();
                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    whatsNew.addAll(listorl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (screenLists.size() > 0) {
                    change_loc_lay.setVisibility(View.GONE);
//                    viewpager_layout.setVisibility(View.VISIBLE);
                    viewPager2.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewall_topdeals.setVisibility(View.VISIBLE);
                } else {
                    change_loc_lay.setVisibility(View.VISIBLE);
//                    viewpager_layout.setVisibility(View.GONE);
                    viewPager2.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewall_topdeals.setVisibility(View.GONE);
                }
                setTabs();
                progressDialog.dismiss();
                screenAdapter.notifyDataSetChanged();
//                tabCounter = screenLists.size()-1;
//                setUpTabClick();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (screenLists.size() > 0) {
                    change_loc_lay.setVisibility(View.GONE);
//                    viewpager_layout.setVisibility(View.VISIBLE);
                    viewPager2.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewall_topdeals.setVisibility(View.VISIBLE);
                } else {
                    change_loc_lay.setVisibility(View.VISIBLE);
//                    viewpager_layout.setVisibility(View.GONE);
                    viewPager2.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewall_topdeals.setVisibility(View.GONE);
                }
                setTabs();
                progressDialog.dismiss();
//                setUpTabClick();
                screenAdapter.notifyDataSetChanged();
//                tabCounter = screenLists.size()-1;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", session_management.getLatPref());
                params.put("lng", session_management.getLangPref());
                params.put("city", session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void setUpTabClick() {
//       if (tabCounter==screenLists.size()-1){
//           screenAdapter.notifyItemChanged(viewPager2.getCurrentItem());
//           tabCounter = 0;
//       }else {
//           tabCounter++;
//       }

    }

    private void DealOfTheDay() {
        dealOftheday.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeDeal, response -> {
            Log.d("HomeTopSelling", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    screenLists.add(new MainScreenList("DEALS OF THE DAY", topSelling, recentSelling, dealOftheday, whatsNew));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCartModel>>() {
                    }.getType();
                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    dealOftheday.addAll(listorl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                whatsNew();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                whatsNew();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", session_management.getLatPref());
                params.put("lng", session_management.getLangPref());
                params.put("city", session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void recentDeal() {
        recentSelling.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeRecent, response -> {
            Log.d("HomeTopSelling", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    screenLists.add(new MainScreenList("RECENT SELLING", topSelling, recentSelling, dealOftheday, whatsNew));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCartModel>>() {
                    }.getType();
                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    recentSelling.addAll(listorl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                DealOfTheDay();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DealOfTheDay();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", session_management.getLatPref());
                params.put("lng", session_management.getLangPref());
                params.put("city", session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.VISIBLE);
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//        fabTwo.setVisibility(View.VISIBLE);
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.VISIBLE);
        fabfour.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//        fabfour.setVisibility(View.VISIBLE);
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.GONE);
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.GONE);
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.GONE);
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.GONE);


    }

    private void closeMenu(boolean value) {
        isMenuOpen = value;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.GONE);
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.GONE);
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.GONE);
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.GONE);


    }

    private void handleFabOne() {
        Log.i(TAG, "handleFabOne: ");
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_clear:
                etSearch.setText("");
                break;
            case R.id.iv_search:
                if (viewPagerStoresProducts.getCurrentItem() == 1) {
                    product("", etSearch.getText().toString().trim());
                } else {
                    selectStores(etSearch.getText().toString().trim());
                }
                break;
            case R.id.fabMain:
                Log.i(TAG, "onClick: fab main");
                if (isMenuOpen) {
                    fabOne.setVisibility(View.GONE);
                    fabTwo.setVisibility(View.GONE);
                    fabThree.setVisibility(View.GONE);
                    fabfour.setVisibility(View.GONE);
                    closeMenu();
                } else {
                    fabOne.setVisibility(View.VISIBLE);
//                    fabTwo.setVisibility(View.VISIBLE);
                    fabThree.setVisibility(View.VISIBLE);
//                    fabfour.setVisibility(View.VISIBLE);
                    openMenu();
                }
                break;
            case R.id.fabOne:
                Intent sendIntent1 = new Intent();
                sendIntent1.setAction(Intent.ACTION_SEND);
                sendIntent1.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
                sendIntent1.setType("text/plain");
                startActivity(sendIntent1);

                Log.i(TAG, "onClick: fab one");
                handleFabOne();
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
           /* case R.id.fabTwo:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                break;*/
            case R.id.fabThree:
                String smsNumber = Splash.configData.getPhone_number();
                openWhatsApp(smsNumber);
                break;


            /*case R.id.fabfour:

                if (isPermissionGranted()) {
                    call_action();
                }

                Log.i(TAG, "onClick: fab four");
                break;*/
        }
    }

    private void openWhatsApp(String numberwhats) {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(numberwhats) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(requireContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = requireContext().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void call_action() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Splash.configData.getPhone_number()));
        startActivity(callIntent);

    }

    /* private void makeGetSliderRequest() {
         imageString.clear();
         String tag_json_obj = "json_category_req";
         Map<String, String> params = new HashMap<String, String>();
         params.put("parent", "");
         CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, BANNER, params,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         Log.d("fghgh", response.toString());
                         try {
                             ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                             JSONArray jsonArray = response.getJSONArray("data");
                             if (jsonArray.length() <= 0) {
                                 recyclerImages.setVisibility(View.GONE);
                             } else {
                                 for (int i = 0; i < jsonArray.length(); i++) {
                                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                                     HashMap<String, String> url_maps = new HashMap<String, String>();
                                     url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                     url_maps.put("banner_id", jsonObject.getString("banner_id"));
                                     url_maps.put("banner_image", BANN_IMG_URL + jsonObject.getString("banner_image"));
                                     imageString.add(BANN_IMG_URL + jsonObject.getString("banner_image"));
                                     listarray.add(url_maps);
                                 }


                                 bannerAdapter.notifyDataSetChanged();


                                 for (HashMap<String, String> name : listarray) {
                                     CustomSlider textSliderView = new CustomSlider(getActivity());
                                     textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                     textSliderView.bundle(new Bundle());
                                     textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                     textSliderView.getBundle().putString("extra", name.get("banner_id"));
 //                                home_list_banner.addSlider(textSliderView);
                                     //   banner_slider.addSlider(textSliderView);
                                     final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                     textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                         @Override
                                         public void onSliderClick(BaseSliderView slider) {
                                             //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
 //                                        Bundle args = new Bundle();
 //                                        android.app.Fragment fm = new Product_fragment();
 //                                        args.putString("id", sub_cat);
 //                                        fm.setArguments(args);
 //                                        FragmentManager fragmentManager = getFragmentManager();
 //                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
 //                                                .addToBackStack(null).commit();
                                         }
                                     });
                                 }
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
             }
         }) {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 HashMap<String, String> param = new HashMap<>();
                 return param;
             }
         };
         RequestQueue requestQueue = Volley.newRequestQueue(getContext());
         requestQueue.getCache().clear();
         requestQueue.add(jsonObjReq);


     }
 */
    private void second_banner() {
        imageString1.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, secondary_banner, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fghgh", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() <= 0) {
                                recyclerImages1.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> url_maps = new HashMap<String, String>();
                                    url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                    url_maps.put("banner_id", jsonObject.getString("sec_banner_id"));
                                    url_maps.put("banner_image", BANNER_IMG_URL + jsonObject.getString("banner_image"));
                                    imageString1.add(BANNER_IMG_URL + jsonObject.getString("banner_image"));
                                    listarray.add(url_maps);
                                }

                                bannerAdapter1.notifyDataSetChanged();

                               /* for (HashMap<String, String> name : listarray) {
                                    CustomSlider textSliderView = new CustomSlider(getActivity());
                                    textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                    textSliderView.getBundle().putString("extra", name.get("banner_id"));
                                   // banner_slider.addSlider(textSliderView);

                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);


    }

    private void makeGetCategoryRequest() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<>();
        params.put("lat", session_management.getLatPref());
        params.put("lng", session_management.getLangPref());
        params.put("city", session_management.getLocationCity());
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.topsix, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Home_adapter(category_modelList);
                            rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (Utils.isOnline(getActivity())) {
            //makeGetSliderRequest();
            //second_banner();
            //makeGetCategoryRequest();
            //topSelling();
            selectStores("");
            product("", "");
            popularStores();
            setUpPopularCategories();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 24) {
            if (data != null && data.getBooleanExtra("open", false)) {
                if (fragmentClickListner != null) {
                    fragmentClickListner.onFragmentClick(data.getBooleanExtra("open", false));
                }
            }
        } else if (requestCode == 56) {
            if (data != null && data.getBooleanExtra("carttogo", false)) {
                if (fragmentClickListner != null) {
                    fragmentClickListner.onFragmentClick(data.getBooleanExtra("carttogo", false));
                }
            } else {
                topSelling();
            }
        } else if (requestCode == 22) {
            if (fragmentClickListner != null) {
                fragmentClickListner.onChangeHome(true);
            }
        }
    }

    private void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}