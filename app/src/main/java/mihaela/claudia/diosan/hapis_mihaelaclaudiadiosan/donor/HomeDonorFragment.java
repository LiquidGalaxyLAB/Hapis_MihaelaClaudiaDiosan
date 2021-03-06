package mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.donor;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.R;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.homeless.Homeless;

import static android.content.Context.MODE_PRIVATE;


public class HomeDonorFragment extends Fragment {

    /*View*/
    private View view;

    /*SharedPreferences*/
    private SharedPreferences preferences;

    /*Firebase*/
    private FirebaseFirestore mFirestore;

    /*SearchView*/
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_donor, container, false);

        initViews();
        firebaseInit();
        buildRecyclerView();

        return view;
    }


    private void initViews(){
        searchView = view.findViewById(R.id.donor_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
    }

    private void buildRecyclerView(){

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view_donor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final List<Homeless> homelesses = new ArrayList<>();

        mFirestore.collection("homeless")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                String image = document.getString("image");
                                final String need = document.getString("homelessNeed");
                                final String address = document.getString("homelessAddress");
                                final String username = document.getString("homelessUsername");

                                final Homeless homeless = new Homeless(username, address, need, image);
                                homelesses.add(homeless);
                                final DonorAdapter donorAdapter = new DonorAdapter(homelesses);
                                searchText(donorAdapter);
                                recyclerView.setAdapter(donorAdapter);

                                recyclerView.setAdapter(donorAdapter);
                                donorAdapter.setOnItemClickListener(new DonorAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("homelessUsername",  homelesses.get(position).getHomelessUsername());
                                        editor.apply();

                                        HomelessFragment homelessFragment = new HomelessFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.donor_fragment_container, homelessFragment)
                                                .addToBackStack(null).commit();
                                    }
                                });

                            }
                        }
                    }
                });


    }

    private void searchText(final DonorAdapter donorAdapter){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    donorAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private void firebaseInit(){
        mFirestore = FirebaseFirestore.getInstance();
    }

}
