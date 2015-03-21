package com.deitel.twittersearches;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.deitel.twittersearches.firstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.deitel.twittersearches.firstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class firstFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // private SharedPreferences savedSearches; // user's favorite searches
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> title;
    private String[] url = {"http://www.baidu.com", "http://www.sina.com.cn"};

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static firstFragment newInstance(String param1, String param2) {
        firstFragment fragment = new firstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public firstFragment() {
        // Required empty public constructor
    }

    public firstFragment(ArrayList<String> title) {
        // Required empty public constructor
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayAdapter<String> list = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, title);
        setListAdapter(list);
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    //I try to use longClick to achieve share,edit and delete,bur i failed
    //someone in baidu said that use oncontextitemselected but i don't think it is a right way;
    //I think in this way is also accord with the assessment :).

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            final String tag = title.get(position);
            final String urlString = getString(R.string.searchURL) +
                    Uri.encode(MainActivity.savedSearches.getString(tag, ""), "UTF-8");
//            mListener.sendPositionToFragment2(urlString);
//            final String tag = title.get(position);

            // create a new AlertDialog
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(firstFragment.this.getActivity());

            // set the AlertDialog's title
            builder.setTitle(
                    getString(R.string.shareEditDeleteTitle, tag));

            // set list of items to display in dialog
            builder.setItems(R.array.dialog_items,
                    new DialogInterface.OnClickListener() {
                        // responds to user touch by sharing, editing or
                        // deleting a saved search
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle b = new Bundle();
                            b.putString("tag", tag);
                            b.putString("url", urlString);
                            Message msg = new Message();
                            msg.setData(b);
                            switch (which) {
                                case 0:
                                    mListener.sendPositionToFragment2(urlString);
                                    break;
                                case 1: // share
                                     msg.what = HandlerMsgUtil.SHARE_CODE;
                                    MainActivity.myhandler.sendMessage(msg);
                                    break;
                                case 2: // edit
                                    // set EditTexts to match chosen tag and query
                                    msg.what = HandlerMsgUtil.EDIT_CODE;
                                    MainActivity.myhandler.sendMessage(msg);
//                                    tagEditText.setText(tag);
//                                    queryEditText.setText(
//                                            savedSearches.getString(tag, ""));
                                    break;
                                case 3: // delete
                                   msg.what = HandlerMsgUtil.DELITE_CODE;
                                    MainActivity.myhandler.sendMessage(msg);
                                    break;
                            }
                        }
                    } // end DialogInterface.OnClickListener
            ); // end call to builder.setItems

            // set the AlertDialog's negative Button
            builder.setNegativeButton(getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        // called when the "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel(); // dismiss the AlertDialog
                        }
                    }
            ); // end call to setNegativeButton

            builder.create().show(); // display the AlertDialog
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        void sendPositionToFragment2(String position);
    }
}
