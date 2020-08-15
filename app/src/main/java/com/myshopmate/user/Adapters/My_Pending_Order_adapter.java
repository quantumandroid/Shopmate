package com.myshopmate.user.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.ModelClass.My_Pending_order_model;
import com.myshopmate.user.ModelClass.NewPendingOrderModel;
import com.myshopmate.user.R;
import com.myshopmate.user.util.CallToDeliveryBoy;
import com.myshopmate.user.util.MyPendingReorderClick;
import com.myshopmate.user.util.Session_management;
import com.myshopmate.user.util.TodayOrderClickListner;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class My_Pending_Order_adapter extends RecyclerView.Adapter<My_Pending_Order_adapter.MyViewHolder> {

    SharedPreferences preferences;
    private List<NewPendingOrderModel> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
    private Context context;
    private Session_management session_management;
    private MyPendingReorderClick myPendingReorderClick;
    private CallToDeliveryBoy callToDeliveryBoy;
    private TodayOrderClickListner todayOrderClickListner;

    public My_Pending_Order_adapter(Context context, List<My_Pending_order_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;
        session_management = new Session_management(context);

    }

    public My_Pending_Order_adapter(List<NewPendingOrderModel> modelList, MyPendingReorderClick myPendingReorderClick, CallToDeliveryBoy callToDeliveryBoy) {
        this.modelList = modelList;
        this.myPendingReorderClick = myPendingReorderClick;
        this.callToDeliveryBoy = callToDeliveryBoy;
    }

    public My_Pending_Order_adapter(List<NewPendingOrderModel> modelList, TodayOrderClickListner todayOrderClickListner) {
        this.modelList = modelList;
        this.todayOrderClickListner = todayOrderClickListner;
    }

    @NonNull
    @Override
    public My_Pending_Order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtem_pendingorder, parent, false);
        context = parent.getContext();
        session_management = new Session_management(context);
        return new My_Pending_Order_adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewPendingOrderModel mList = modelList.get(position);
        holder.reorder_btn.setVisibility(View.GONE);
        holder.tv_orderno.setText(mList.getCart_id());
        if (mList.getOrder_status().equalsIgnoreCase("Completed")) {
            holder.relative_background.setCardBackgroundColor(getColor(0, 128, 0));
            holder.relativetextstatus.setText("Completed");
            holder.l1.setVisibility(View.VISIBLE);
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.GONE);
            holder.Delivered.setVisibility(View.GONE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.VISIBLE);
            holder.Delivered1.setVisibility(View.VISIBLE);

        } else if (mList.getOrder_status().equalsIgnoreCase("Pending")) {
            holder.relativetextstatus.setText("Pending");
            holder.l1.setVisibility(View.VISIBLE);
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde.setVisibility(View.VISIBLE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.GONE);
            holder.Out_For_Deliverde1.setVisibility(View.GONE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status().equalsIgnoreCase("Confirmed")) {
            holder.relativetextstatus.setText("Confirmed");
            holder.l1.setVisibility(View.VISIBLE);
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.VISIBLE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.GONE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status().equalsIgnoreCase("Out_For_Delivery")) {
            holder.relativetextstatus.setText("Out For Delivery");
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.l1.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.GONE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.VISIBLE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status().equalsIgnoreCase("Cancelled")) {
            holder.relative_background.setCardBackgroundColor(getColor(255, 0, 0));
            holder.relativetextstatus.setText("Cancelled");
//            holder.btn_lay.setVisibility(View.GONE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.reorder_btn.setVisibility(View.VISIBLE);
            holder.order_details.setVisibility(View.VISIBLE);
            holder.l1.setVisibility(View.GONE);
        }

        if (mList.getPayment_status() == null) {
            holder.tv_status.setText("Payment" + " " + "Pending");
        } else {
            if (mList.getPayment_status().equalsIgnoreCase("success") || mList.getPayment_status().equalsIgnoreCase("failed") || mList.getPayment_status().equalsIgnoreCase("COD")) {
                holder.tv_status.setText("Payment" + " " + mList.getPayment_status());
            }
        }

        if (mList.getPaid_by_wallet() != null && !mList.getPaid_by_wallet().equalsIgnoreCase("") && !mList.getPaid_by_wallet().equalsIgnoreCase("0")) {
            holder.wallet_layout.setVisibility(View.VISIBLE);
            holder.tv_wallet_amount.setText("- " + session_management.getCurrency() + "" + mList.getPaid_by_wallet());
        } else {
            holder.wallet_layout.setVisibility(View.GONE);
        }

        if (mList.getCoupon_discount() != null && !mList.getCoupon_discount().equalsIgnoreCase("") && !mList.getCoupon_discount().equalsIgnoreCase("0")) {
            holder.coupon_layout.setVisibility(View.VISIBLE);
            holder.tv_coupon_amount.setText("- " + session_management.getCurrency() + "" + mList.getCoupon_discount());
        } else {
            holder.coupon_layout.setVisibility(View.GONE);
        }

        if (mList.getDel_charge() != null && !mList.getDel_charge().equalsIgnoreCase("")) {
            holder.tv_delivery_amount.setText(session_management.getCurrency() + "" + mList.getDel_charge());
            holder.tv_order_price_2.setText(session_management.getCurrency() + "" + ((int) (Double.parseDouble(mList.getPrice()) - Double.parseDouble(mList.getDel_charge()))));
        } else {
            holder.tv_order_price_2.setText(session_management.getCurrency() + "" + mList.getPrice());
            holder.tv_delivery_amount.setText(session_management.getCurrency() + " 0");
        }

        if (mList.getDboy_name() != null && !mList.getDboy_name().equalsIgnoreCase("")) {
            holder.order_assing_lay.setVisibility(View.VISIBLE);
            holder.iv_order_boy_name.setText(mList.getDboy_name());
            holder.iv_delivery_number.setText(mList.getDboy_phone());
        } else {
            holder.order_assing_lay.setVisibility(View.GONE);
        }

        holder.iv_order_detail_img.setOnClickListener(v -> {
            if (holder.delivery_boy_details.getVisibility() == View.VISIBLE) {
                holder.delivery_boy_details.setVisibility(View.GONE);
            } else {
                holder.delivery_boy_details.setVisibility(View.VISIBLE);
            }
        });

        holder.iv_call_order.setOnClickListener(v -> todayOrderClickListner.onCallToDeliveryBoy(mList.getDboy_phone()));

        holder.info_price.setOnClickListener(v -> {
            if (holder.price_deatils.getVisibility() == View.VISIBLE) {
                holder.price_deatils.setVisibility(View.GONE);
            } else {
                holder.price_deatils.setVisibility(View.VISIBLE);
            }
        });


        holder.tv_pending_date.setText(mList.getDelivery_date());
        holder.tv_confirm_date.setText(mList.getDelivery_date());
        holder.tv_delevered_date.setText(mList.getDelivery_date());
        holder.tv_cancel_date.setText(mList.getDelivery_date());

        if (mList.getPayment_method().equals("Store Pick Up")) {
            holder.tv_methid1.setText(mList.getPayment_method());
        } else if (mList.getPayment_method().equalsIgnoreCase("COD")) {
            holder.tv_methid1.setText("Cash On Delivery");
        } else if (mList.getPayment_method().equalsIgnoreCase("Cards")) {
            holder.tv_methid1.setText("PrePaid");
        } else if (mList.getPayment_method().equalsIgnoreCase("net_banking")) {
            holder.tv_methid1.setText("PrePaid");
        } else if (mList.getPayment_method().equalsIgnoreCase("Wallet")) {
            holder.tv_methid1.setText("Wallet");
        }


//        if (mList.getOrder_status().equals("0")) {
//            holder.relativetextstatus.setText("Delivered");
//
//            holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
//            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        } else if (mList.getOrder_status().equals("1")) {
//            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
//            holder.Confirm.setImageResource(R.color.green);
//            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
//            holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
//            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
//        } else if (mList.getOrder_status().equals("2")) {
//            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.purple));
//            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.view3.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.view4.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.Confirm.setImageResource(R.color.green);
//            holder.Out_For_Deliverde.setImageResource(R.color.green);
//            holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
//            holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
//            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
//        } else if (mList.getOrder_status().equals("4")) {
//            holder.linearLayout.setVisibility(View.GONE);
//        }


        holder.tv_date.setText(mList.getDelivery_date());
        holder.tv_tracking_date.setText(mList.getDelivery_date());

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language = preferences.getString("language", "");

        if (language.contains("spanish")) {
            String timefrom = mList.getTime_slot();


            timefrom = timefrom.replace("pm", "م");
            timefrom = timefrom.replace("am", "ص");


            String time = timefrom;

            holder.tv_time.setText(time);
        } else {
            holder.tv_time.setText(mList.getTime_slot());
        }

        holder.tv_price.setText(session_management.getCurrency() + "" + mList.getPrice());
        if (mList.getRemaining_amount() != null && !mList.getRemaining_amount().equalsIgnoreCase("")) {
            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" + mList.getRemaining_amount());
            holder.tv_total_pay.setText(session_management.getCurrency() + "" + mList.getRemaining_amount());
        } else {
            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" + mList.getPrice());
        }

        holder.tv_item.setText("" + mList.getData().size());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.reorder_btn.setBackgroundTintList(ColorStateList.valueOf(getColor(254,129,0)));
//        }else {
//
//        }
//        holder.reorder_btn.getBackground().setColorFilter(getColor(254,129,0), PorterDuff.Mode.SRC_ATOP);
//        holder.reorder_btn.setText("Order Detail's");
        holder.order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayOrderClickListner.onClickForOrderDetails(holder.getAdapterPosition(),"pending");
            }
        });

        holder.reorder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayOrderClickListner.onReorderClick(position,"pending");
            }
        });

        holder.canclebtn.setOnClickListener(v -> {
//            showDeleteDialog(position);
            todayOrderClickListner.onCancelClick(position,"pending");
        });

    }

    public void removeddata(int postion) {
        modelList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion, getItemCount());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public int getColor(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date, tv_pay_ableamount, tv_order_price_2, tv_wallet_amount, tv_coupon_amount, tv_delivery_amount, tv_total_pay;
        public TextView tv_pending_date, tv_pending_time, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
        public View view1, view2, view3, view4, view5, view6;
        public CardView relative_background;
        public LinearLayout rr, price_deatils;
        public CircleImageView Confirm, Out_For_Deliverde, Delivered;
        public CircleImageView Confirm1, Out_For_Deliverde1, Delivered1;
        public TextView tv_methid1;
        public ImageView info_price;
        public String method;
        //        CardView cardView;
        TextView canclebtn, reorder_btn, order_details;

        TextView iv_order_boy_name, iv_delivery_number;
        LinearLayout delivery_boy_details, order_assing_lay;
        ImageView iv_call_order, iv_order_detail_img;
        //        LinearLayout linearLayout;
        LinearLayout l1;
        LinearLayout btn_lay;
        LinearLayout wallet_layout, coupon_layout, delivery_layout;

        public MyViewHolder(View view) {

            super(view);
            tv_orderno = view.findViewById(R.id.tv_order_no);
            tv_pay_ableamount = view.findViewById(R.id.tv_pay_ableamount);
            order_assing_lay = view.findViewById(R.id.order_assing_lay);
            iv_order_detail_img = view.findViewById(R.id.iv_order_detail_img);
            iv_delivery_number = view.findViewById(R.id.iv_delivery_number);
            iv_order_boy_name = view.findViewById(R.id.iv_order_boy_name);
            delivery_boy_details = view.findViewById(R.id.delivery_boy_details);
            iv_call_order = view.findViewById(R.id.iv_call_order);
            tv_order_price_2 = view.findViewById(R.id.tv_order_price_2);
            tv_coupon_amount = view.findViewById(R.id.tv_coupon_amount);
            tv_total_pay = view.findViewById(R.id.tv_total_pay);
            tv_delivery_amount = view.findViewById(R.id.tv_delivery_amount);
            tv_wallet_amount = view.findViewById(R.id.tv_wallet_amount);
            delivery_layout = view.findViewById(R.id.delivery_layout);
            wallet_layout = view.findViewById(R.id.wallet_layout);
            coupon_layout = view.findViewById(R.id.coupon_layout);
            price_deatils = view.findViewById(R.id.price_deatils);
            info_price = view.findViewById(R.id.info_price);
            tv_status = view.findViewById(R.id.tv_order_status);
            relativetextstatus = view.findViewById(R.id.status);
            tv_tracking_date = view.findViewById(R.id.tracking_date);
            tv_date = view.findViewById(R.id.tv_order_date);
            tv_time = view.findViewById(R.id.tv_order_time);
            tv_price = view.findViewById(R.id.tv_order_price);
            tv_item = view.findViewById(R.id.tv_order_item);
            canclebtn = view.findViewById(R.id.canclebtn);
            reorder_btn = view.findViewById(R.id.reorder_btn);
            l1 = view.findViewById(R.id.l1);
            btn_lay = view.findViewById(R.id.btn_lay);
            rr = view.findViewById(R.id.rrrr);
            order_details = view.findViewById(R.id.order_details);

//            cardView = view.findViewById(R.id.card_view);

//            linearLayout = view.findViewById(R.id.l2);
//            //Payment Method
            tv_methid1 = view.findViewById(R.id.method1);
            //Date And Time
            tv_pending_date = view.findViewById(R.id.pending_date);
//            tv_pending_time = (TextView) view.findViewById(R.id.pending_time);
            tv_confirm_date = view.findViewById(R.id.confirm_date);
//            tv_confirm_time = (TextView) view.findViewById(R.id.confirm_time);
            tv_delevered_date = view.findViewById(R.id.delevered_date);
//            tv_delevered_time = (TextView) view.findViewById(R.id.delevered_time);
            tv_cancel_date = view.findViewById(R.id.cancel_date);
//            tv_cancel_time = (TextView) view.findViewById(R.id.cancel_time);
            //Oredre Tracking
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view3 = view.findViewById(R.id.view3);
            view4 = view.findViewById(R.id.view4);
            view5 = view.findViewById(R.id.view5);
            view6 = view.findViewById(R.id.view6);
            relative_background = view.findViewById(R.id.relative_background);

            Confirm = view.findViewById(R.id.confirm_image);
            Out_For_Deliverde = view.findViewById(R.id.delivered_image);
            Delivered = view.findViewById(R.id.cancal_image);
            Confirm1 = view.findViewById(R.id.confirm_image1);
            Out_For_Deliverde1 = view.findViewById(R.id.delivered_image1);
            Delivered1 = view.findViewById(R.id.cancal_image1);
        }
    }

}

