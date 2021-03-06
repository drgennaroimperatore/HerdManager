package com.ilri.herdmanager.ui.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.database.entities.Farmer;

public class FarmerRadioButton extends BaseRadioButton {
   private TextView mTitleTextView, mRegionTextView, mDistrictTextView, mKebeleTextView;
   private Farmer mFarmerAssociatedWithButton;




   public FarmerRadioButton (Context context)
   {
       super(context, R.layout.custom_farmer_radio_button, R.styleable.FarmerRadioButton);

   }

   public FarmerRadioButton (Context context, AttributeSet attr)
   {
       super(context, R.layout.custom_farmer_radio_button, R.styleable.FarmerRadioButton);
       initAttributes();
   }

   public void setFarmerInformation (Farmer farmer)
   {
       mFarmerAssociatedWithButton = farmer;

       bindViews();
       mTitleTextView.setText(farmer.firstName+ " " + farmer.secondName);
       mRegionTextView.setText(farmer.region);
       mDistrictTextView.setText(farmer.district);
       mKebeleTextView.setText(farmer.kebele);


       RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       setLayoutParams(lp);


   }

   public Farmer getAssociatedFarmer() {return mFarmerAssociatedWithButton;}



   public FarmerRadioButton(Context context, AttributeSet attrs, int layoutResId, int[] styleable) {
        super(context, attrs, R.layout.custom_farmer_radio_button, R.styleable.FarmerRadioButton);
    }

    public FarmerRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int layoutResId, int[] styleable) {
        super(context, attrs, defStyleAttr, R.layout.custom_farmer_radio_button, R.styleable.FarmerRadioButton);
    }

    @Override
    protected void bindViews() {
     mTitleTextView = findViewById(R.id.radio_button_farmerName);
     mRegionTextView = findViewById(R.id.radio_button_farmerRegion);
     mDistrictTextView = findViewById(R.id.radio_button_farmerDistrict);
     mKebeleTextView = findViewById(R.id.radio_button_farmerKebele);

    }

    @Override
    protected void initAttributes() {
     int index = R.styleable.FarmerRadioButton_farmer_rb_farmer_name;

     mTitleTextView.setText("Test");
     if(typedArrayHasValue(index)) {
      String title = a.getString(index);
      mTitleTextView.setText(title);
     }

    }

 private boolean typedArrayHasValue(int index) {
  return a.hasValue(index);
 }




 @Override
    protected void populateViews() {

    }
}
