#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>
#include <fstream>

using namespace std;

bool contain(string s, char c)
{
    return s.find(c) != string::npos;
}

// vy --> vi, thy --> thi
// Rule: any 'y' at the end of word, follow a consonant
// Input: single word lower case
string y_to_i(string str)
{
    // string vowels="aăâeêioôơuưy";
    string phu_am = "bcdđghklmnpqrstvx";
    string result = str;
    if (str.length() >= 2 && str[str.length() - 1] == 'y' && phu_am.find(str[str.length() - 2]) != string::npos)
    {
        result[str.length() - 1] = 'i';
    }
    return result;
}

string lowercase(string s)
{
    string result = s;
    for (char &c : result)
    {
        c = tolower(c);
    }
    return result;
}

string normalize(string s)
{
    return y_to_i(lowercase(s));
}

// Split string into tokens seperated by delimiters. Empty tokens are ignored
vector<string> tokenize(string str, string delims = " .()/|:;,")
{
    vector<string> tokens;
    string token = "";

    for (char c : str)
    {
        if (contain(delims, c))
        {
            if (token.length() > 0)
            {
                tokens.push_back(token);
            }
            token = "";
        }
        else
        {
            token += c;
        }
    }
    if (token.length() > 0)
        tokens.push_back(token);

    return tokens;
}

vector<string> tokenize_normalize(string s) {
    vector<string> tokens = tokenize(s);
    for (string &token : tokens)
        token = normalize(token);
    return tokens;
}

// float match_token(string t1,string t2) {

// }
// TODO: sub has 2 same words
bool sub_array(vector<string> v, vector<string> sub) {
    for (string word:sub) {
        if (find(v.begin(), v.end(), word) == v.end()) 
            return false;
    }
    return true;
}

vector<string> match_contact(string query_name, const vector<string> &contact)
{
    vector<string> result;
    vector<string> query_tokens = tokenize_normalize(query_name);

    for (string contact_name : contact)
    {
        vector<string> contact_tokens = tokenize_normalize(contact_name);

        if (sub_array(contact_tokens,query_tokens) or sub_array(query_tokens,contact_tokens)) {
            result.push_back(contact_name);
        }
    }
    return result;
}

int main()
{
    // string str = "thy";
    // cout << y_to_i(str);
    // std::string s;
    // while (true)
    // {
    //     getline(cin, s);
    //     if (s.length() == 0)
    //     {
    //         return 0;
    //     }
    //     // vector<string> t = tokenize(s," .(\t");
    //     // for (string s:t) {
    //     // cout<<s<<"|";
    //     // }
    //     cout << lowercase(s) << endl;

    //     cout << endl;
    // }
    std::ifstream file("contact");
    vector<string> contact;
    string line;
    while (getline(file,line))
    {
        contact.push_back(line);
    }
    // for (auto name:contact) {
    //     cout<< name<<" * ";
    // }
    // vector<string> contact = {"HT Tony", "Anh Hiếu", "Anh Hưng Thịnh","Ba","HT Bibiluck","Family Anh Thiên","TP Anh Thư","VT Trinh","ANZ Tâm","Bình Lùn","Bình Gà"};   
    cout<<"Enter name:";
    string query_name;
    getline(cin,query_name);
    for (auto match_name:match_contact(query_name, contact))
        cout<< match_name <<endl;

    return 0;
}
