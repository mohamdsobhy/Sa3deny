package com.codingwithmitch.googlemaps2018.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingwithmitch.googlemaps2018.R;
import com.codingwithmitch.googlemaps2018.UserClient;
import com.codingwithmitch.googlemaps2018.models.User;
import com.codingwithmitch.googlemaps2018.ui.IProfile;
import com.codingwithmitch.googlemaps2018.ui.ImageListFragment;
import com.codingwithmitch.googlemaps2018.ui.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements
        View.OnClickListener,
        IProfile {

    private static final String TAG = "ProfileActivity";


    //widgets
    private CircleImageView mAvatarImage;

    //vars
    private ImageListFragment mImageListFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAvatarImage = view.findViewById(R.id.image_choose_avatar);

        view.findViewById(R.id.image_choose_avatar).setOnClickListener(this);
        view.findViewById(R.id.text_choose_avatar).setOnClickListener(this);

        retrieveProfileImage();




        return view;
    }

    private void retrieveProfileImage(){
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.cwm_logo)
                .placeholder(R.drawable.cwm_logo);

        int avatar = 0;
        try{
            avatar = Integer.parseInt(((UserClient)getActivity().getApplicationContext()).getUser().getAvatar());
        }catch (NumberFormatException e){
            Log.e(TAG, "retrieveProfileImage: no avatar image. Setting default. " + e.getMessage() );
        }

        Glide.with(getActivity())
                .setDefaultRequestOptions(requestOptions)
                .load(avatar)
                .into(mAvatarImage);
    }

    @Override
    public void onClick(View v) {
        mImageListFragment = new ImageListFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                .replace(R.id.fragment_container, mImageListFragment, getString(R.string.fragment_image_list))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{
                getActivity().finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.getActivity().onCreateOptionsMenu(menu);
    }

    @Override
    public void onImageSelected(int resource) {

        // remove the image selector fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                .remove(mImageListFragment)
                .commit();

        // display the image
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.cwm_logo)
                .error(R.drawable.cwm_logo);

        Glide.with(getActivity())
                .setDefaultRequestOptions(requestOptions)
                .load(resource)
                .into(mAvatarImage);

        // update the client and database
        User user = ((UserClient)getActivity().getApplicationContext()).getUser();
        user.setAvatar(String.valueOf(resource));

        FirebaseFirestore.getInstance()
                .collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid())
                .set(user);
    }

}
