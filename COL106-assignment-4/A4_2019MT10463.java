import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class A4_2019MT10463 {
    
    protected static ArrayList<HashMap<Integer, Long>> ad; // adjacency list
    private static HashMap<String, Integer> names = new HashMap<>(); // hashmap for names and ids
    private static HashMap<String, Integer> names_to_read_edges = new HashMap<>(); // name with quotes, rest same as names
    private static ArrayList<String> ids; // id to name (reverse of "names");
    private static int[] degree; // degree of node given id
    private static Long[] cooccurrences; // cooccurrences of node given id
    private static ArrayList<Integer> rank_list;
    private static boolean[] vis;
    protected static ArrayList<ArrayList<Integer>> component_nodes;

    protected static int n, m; // no. of nodes and edges resp.
    private static int id = 0;

    public static double average() {
        double res = 0d;
        if(n == 0) {
            return 0;
        }
        for(int i = 0;i < n;i ++) {
            res = res + (double)degree[i];
        }
        res = res / (double)n;
        return res; 
    }
    /*
     * @params:
     * [l, r) = inteval to sort
     * returns: void
     * Sorts rank_list in descending order
     * Tie breaking: by lexicographic order of string
     * Further Tie Breaking: Stable sort ensures nodes mentioned first in nodes.csv come first
    */
    private static void mergeSort(int l, int r) {
        if (l >= r-1) return;
        if (l >= rank_list.size()) return;
        int mid = (l + r) / 2;
        mergeSort(l, mid);
        mergeSort(mid, r);
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int i = l;
        int j = mid;

        while (i < mid && j < r) {
            int tk;
            if (cooccurrences[rank_list.get(i)] > cooccurrences[rank_list.get(j)]) tk = 1;
            else if(cooccurrences[rank_list.get(i)] < cooccurrences[rank_list.get(j)]) tk = -1;
            else tk = 0;
            if (tk > 0) {
                temp.add(rank_list.get(i));
                i++;
            } else if(tk == -1) {
                temp.add(rank_list.get(j));
                j++;
            } else {
                int tk2 = ids.get(rank_list.get(i)).compareTo(ids.get(rank_list.get(j)));
                if (tk2 >= 0) {
                    temp.add(rank_list.get(i));
                    i ++;
                }else {
                    temp.add(rank_list.get(j));
                    j ++;
                }
            }
        }
        while(i < mid) {
            temp.add(rank_list.get(i));
            i++;
        }
        while(j < r) {
            temp.add(rank_list.get(j));
            j++;
        }

        for (int ii = l;ii < r; ii ++) {
            rank_list.set(ii, temp.get(ii-l));
        }

    }

    public static void rank() {
        System.err.println("<RANK>");
        rank_list = new ArrayList<Integer>();
        for(int i = 0;i < n;i++) {
            rank_list.add(i);
        }
        // rank_list ready to be sorted

        mergeSort(0, n);
        for(int i = 0;i < n; i ++) {
            int id = rank_list.get(i);

            System.out.print(ids.get(id));
            if (i < n-1) System.out.print(",");
        }
        
        System.out.println("");
        System.err.println("<\\RANK>");
    }

    private static void mergeSortEachListCompNodes(int ind, int l, int r) {
        if(ind >= component_nodes.size()) {
            return;
        }
        if (l >= r-1) return;
        if (l >= component_nodes.get(ind).size()) return;
        int m = (l + r) / 2;
        mergeSortEachListCompNodes(ind, l, m);
        mergeSortEachListCompNodes(ind, m, r);

        ArrayList<Integer> temp = new ArrayList<Integer>();
        int i = l;
        int j = m;
        while(i < m && j < r) {
            int tk = ids.get(component_nodes.get(ind).get(i)).compareTo(ids.get(component_nodes.get(ind).get(j)));

            if (tk >= 0) {
                if (tk == 0) {
                    System.err.println("ABORT! same name detected while sorting each connected component\n");
                }
                temp.add(component_nodes.get(ind).get(i));
                i ++;
            } else {
                temp.add(component_nodes.get(ind).get(j));
                j ++;
            }
        }
        while(i < m) {
            temp.add(component_nodes.get(ind).get(i));
            i ++;
        }
        while(j < r) {
            temp.add(component_nodes.get(ind).get(j));
            j ++;
        }

        for (int ii = l;ii < r;ii ++) {
            component_nodes.get(ind).set(ii, temp.get(ii - l));
        }

    }

    private static void mergeSortCompNodes(int l, int r) {
        if (l >= r-1) return;
        if (l >= component_nodes.size()) return;

        int m = (l + r) / 2;
        mergeSortCompNodes(l, m);
        mergeSortCompNodes(m, r);

        ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
        int i = l;
        int j = m;
        while(i < m && j < r) {
            int tk;
            if (component_nodes.get(i).size() > component_nodes.get(j).size()) tk = 1;
            else if(component_nodes.get(i).size() < component_nodes.get(j).size()) tk = -1;
            else tk = 0;
            if(tk == 0) {
                if(component_nodes.get(i).size() == 0) {
                    System.err.println("WARNING! 0 size components detected\n");
                }else {
                    int q = 0;
                    while(q < component_nodes.get(i).size() && q < component_nodes.get(j).size()) {
                        tk = ids.get(component_nodes.get(i).get(q)).compareTo(ids.get(component_nodes.get(j).get(q)));
                        if (tk != 0) break;

                        q++;
                    }
                    if(ids.get(component_nodes.get(i).get(0)).compareTo(ids.get(component_nodes.get(j).get(0))) == 0) {
                        System.err.println("ABORT! Same first node in two components of same size\n");
                    }
                    if(tk == 0) {
                        System.err.println("ABORT! Exact same components detected\n");
                    }
                }
            }
            if(tk >= 0) {
                temp.add(component_nodes.get(i));
                i ++;
            }else if(tk < 0) {
                temp.add(component_nodes.get(j));
                j ++;
            }
        }
        while (i < m) {
            temp.add(component_nodes.get(i));
            i ++;
        }
        while (j < r) {
            temp.add(component_nodes.get(j));
            j ++;
        }

        for (int ii = l;ii < r;ii ++) {
            component_nodes.set(ii, temp.get(ii - l));
        }

    }
    public static void independent_storylines_dfs(int i) {
        vis[i] = true;
        component_nodes.get(component_nodes.size() - 1).add(i);
        ad.get(i).forEach((k, v) -> {
            if (!vis[k]) {
                independent_storylines_dfs(k);
            }
        });
    }

    private static String parse_node_input(String in) {
        int n = in.length();
        boolean start_quotes = (in.charAt(0) == '"');
        boolean end_quotes = (in.charAt(n-1) == '"');
        int num_commas = 0;
        int i_comma = -1;
        for(int i = 0;i < n;i ++) {
            if(in.charAt(i) != ',') continue;
            num_commas ++;
            i_comma = i;
        }
        if (num_commas == 1) {
            if(i_comma == n-1) return "";
            return in.substring(i_comma + 1, n);
        }
        String res = in.substring(n/2 +1, n);
        i_comma = -1;
        int num_commas2 = 0;
        int num_quotes = 0;
        for(int i = 0;i < n;i ++) {
            if(in.charAt(i) == '"') num_quotes ++;
            if(in.charAt(i) != ',') continue;

            num_commas2++;
            if (i > 1 && in.charAt(i-1) == '"' && start_quotes) {
                if(i_comma != -1 /*&& (num_quotes % 2 == 0)*/) {
                    // found separating comma
                    res = in.substring(i+1, n);
                    break;
                }
            }
            if (i < n-2 && in.charAt(i+1) == '"' && end_quotes) {
                if (num_commas2 < num_commas /*&& (num_quotes % 2 == 0)*/) {
                    // found separating comma
                    res = in.substring(i+1, n);
                    break;
                }
            }
            i_comma = i;
        }
        // System.err.println(res);
        return res;
    }

    //////////////////////////////////////////// MAIN ////////////////////////////////////////////
    public static void main(String[] args) throws IOException {

        ad = new ArrayList<HashMap<Integer, Long> >();
        names = new HashMap<String, Integer>();
        names_to_read_edges = new HashMap<String, Integer>();
        ids = new ArrayList<String>();
        component_nodes = new ArrayList<ArrayList<Integer>>();
        n = 0;
        m = 0;
        id = 0;
        
        String nodes_file_name = args[0];
        String edges_file_name = args[1];
        String fun_name = args[2];

        File nodes = new File(nodes_file_name);
        File edges = new File(edges_file_name);

        BufferedReader nodes_reader = new BufferedReader(new FileReader(nodes));
        BufferedReader edges_reader = new BufferedReader(new FileReader(edges));

        // Reading nodes; skipping a line since it is guaranteed that headers will be included
        System.err.println("Reading nodes");
        String st = nodes_reader.readLine(); // header
        while ((st = nodes_reader.readLine()) != null) {
            if(st.length() == 0) continue;
            String s = parse_node_input(st);
            if(s.length() == 0) {
                System.err.println("Empty string at line"+id+1);
            }
            if(names_to_read_edges.containsKey(s)) continue;

            ++ n;
            int n_st = s.length();
            names_to_read_edges.put(s, id); // assign s an id

            boolean is_comma = false; // finding a comma in the name
            for(int i = 0;i < n_st; ++i) {
                if(s.charAt(i) == ',') {
                    is_comma = true;
                    break;
                }
            }
            if(is_comma && n_st > 1) s = s.substring(1, n_st-1); // remove wrapping quotes
            names.put(s, id); // put real name in names
            ids.add(s); // ids.get(id) = s

            ad.add(new HashMap<Integer, Long>());
            id++; // increment id
        }
        System.err.println("Done! " + n + " nodes read");
        // finished reading nodes

        degree = new int[n];
        cooccurrences = new Long[n];
        vis = new boolean[n];
        for(int i = 0;i < n;i ++) {
            cooccurrences[i] = 0L;
            degree[i] = 0;
            vis[i] = false;
        }

        // Reading edges.csv; skipping a line since it is guaranteed that headers will be included
        System.err.println("Reading edges");
        st = edges_reader.readLine();
        while ((st = edges_reader.readLine()) != null) {
            int n_st = st.length();
            if(n_st == 0) continue;

            // find weight of edge: in the last part of edge
            int num = n_st-1;
            while(num >= 0) {
                if(st.charAt(num) == ',') break;
                num--;
            }
            Long weight = Long.valueOf(st.substring(num+1, n_st)); // could be huge
            if (weight <= 0) continue; // redundant/bad edge

            // [0, num) contains endpoints of edge
            // find the splitting comma
            int num_commas = 0; // assuming small
            boolean read_edge = false;
            for (int i = 0;i < num;i ++) {
                if (st.charAt(i) == ',') {
                    num_commas ++;
                    
                    String name1 = st.substring(0, i);
                    String name2 = st.substring(i+1, num);

                    Integer id1 = names_to_read_edges.get(name1);
                    Integer id2 = names_to_read_edges.get(name2);

                    if (id1 == null || id2 == null) continue; 

                    // got 'em
                    if(ad.get(id1).containsKey(id2)) {
                        System.err.println("ABORT!! Multiedge detected b/w " + ids.get(id1) + " and " + ids.get(id2));
                    }
                    if(id1 == id2) {
                        System.err.println("ABORT!! Self Loop detected b/w " + ids.get(id1));
                    }
                    ad.get(id1).put(id2, weight); cooccurrences[id1] += weight; degree[id1] ++;
                    ad.get(id2).put(id1, weight); cooccurrences[id2] += weight; degree[id2] ++;
                    ++ m;
                    read_edge = true;
                    break;
                }
            }
            if(!read_edge) {
                System.err.println("EDGE SKIPPED!");
            }

        }
        System.err.println("Done! " + m +" edges read");

        nodes_reader.close();
        edges_reader.close();
        // graph ready

        System.err.println("Given function name: " + fun_name);
        if (fun_name.equals("average")) {
            double res = average();
            System.out.printf("%.2f", res);
            System.err.println("");
        } else if(fun_name.equals("rank")) {
            rank();
        } else if(fun_name.equals("independent_storylines_dfs")) {
            for (int i = 0;i < n;i ++) {
                if (vis[i]) continue;
                component_nodes.add(new ArrayList<Integer>());
                independent_storylines_dfs(i);
            }
            
            int n_compNodes = component_nodes.size();
            for(int i = 0;i < n_compNodes;i ++) {
                int n_list = component_nodes.get(i).size();
                mergeSortEachListCompNodes(i, 0, n_list);
            }
            mergeSortCompNodes(0, n_compNodes);

            for (int i = 0;i < n_compNodes;i ++) {
                if(component_nodes.get(i).size() == 0) {
                    System.err.println("0 size component");
                    continue;
                }
                for (int j = 0;j < component_nodes.get(i).size();j ++) {
                    System.out.print(ids.get(component_nodes.get(i).get(j)));
                    if(j < component_nodes.get(i).size()-1) System.out.print(",");
                }
                System.out.println("");
            }
        }
        
    }
}