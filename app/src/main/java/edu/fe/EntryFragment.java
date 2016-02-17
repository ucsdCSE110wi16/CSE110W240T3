package edu.fe;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import edu.fe.backend.Category;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntryFragment#create} factory method to
 * create an instance of this fragment.
 */
public class EntryFragment extends DialogFragment {

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
    public static EntryFragment create(boolean darkTheme, int accentColor) {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        args.putBoolean("dark_theme", darkTheme);
        args.putInt("accent_color",accentColor);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_entry, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }

        Spinner spinner = (Spinner) customView.findViewById(R.id.spinner);



        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_array, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter = new SpinAdapter(this.getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(getArguments().getBoolean("dark_theme") ? Theme.DARK : Theme.LIGHT)
                .title(R.string.entryPopUp)
                .customView(customView, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build();

        return dialog;
    }

    //ignore for now
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    */

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_entry, container, false);

        //getDialog().setTitle("Food entry");
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
*/


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
