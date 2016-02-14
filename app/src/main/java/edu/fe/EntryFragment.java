package edu.fe;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import edu.fe.util.FoodItem;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryFragment extends DialogFragment {

    int mNum;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryFragment newInstance(int num) {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        int style = DialogFragment.STYLE_NORMAL, theme =android.R.style.Theme_Holo_Light_Dialog;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        //TODO These don't seem to work, will come back to update to material
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Material; break;
            case 5: theme = android.R.style.Theme_Material_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Material_Light; break;
            case 7: theme = android.R.style.Theme_Material_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Material_Light; break;
        }
        setStyle(style, theme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_entry, container, false);

        getDialog().setTitle("Food entry");
        getDialog().setCanceledOnTouchOutside(true);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        Button sBtn = (Button) view.findViewById(R.id.btnSubmit);
        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tentative action. Only closes the dialog
                getActivity().getFragmentManager().beginTransaction().remove(EntryFragment.this).commit();
            }
        });

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDialogFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {super.onAttach(context);}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        //DisplayMetrics metrics = new DisplayMetrics();
        //Window window = getDialog().getWindow();
        //window.setGravity(Gravity.CENTER);
        //window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //getDialog().getWindow().setLayout(1000, 1500);
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
        //TODO: Update argument type and name
        void onDialogFragmentInteraction(Uri uri);
    }
}
