#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>
#include <fstream>
#include <tuple>

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
    string phu_am = "bcdđghklmnpqrstvx";
    string result = str;
    if (str.length() >= 2 && str[str.length() - 1] == 'y' && phu_am.find(str[str.length() - 2]) != string::npos) {
        result[str.length() - 1] = 'i';
    }
    return result;
}

// Lowercase a string
string lowercase(string s)
{
    string result = s;
    for (char &c : result){
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

float match_token(string t1,string t2) {
    if (t1==t2)
        return 1.0;
    else
        return -1;
}

// TODO: sub has 2 same words
// TODO: order
float match_token_array(vector<string> v1, vector<string> v2) {
    bool v2_contain_v1 = false;
    float sum_score=0.0;

    for (string token1:v1) {
        bool has_token1 = false;
        for (string token2:v2) {
            float match_score=match_token(token1,token2);
            if (match_score>0){
                has_token1=true;
                sum_score+=match_score;
            }
        }
        if (has_token1==false) 
            return -1;
    }
    return sum_score/v2.size();
}

vector<tuple<string,float>> match_contact(string query_name, const vector<string> &contact)
{
    vector<tuple<string,float>> result;
    vector<string> query_tokens = tokenize_normalize(query_name);

    for (string contact_name : contact)
    {
        vector<string> contact_tokens = tokenize_normalize(contact_name);
        float match_score = match_token_array(query_tokens,contact_tokens);
        if (match_score>0){
            result.push_back(make_tuple(contact_name,match_score));
        }
    }
    return result;
}

int main()
{
    std::ifstream file("contact");
    vector<string> contact;
    string line;
    while (getline(file,line))
    {
        contact.push_back(line);
    }
    cout<<"Enter name:";
    string query_name;
    getline(cin,query_name);
    for (auto match_name:match_contact(query_name, contact))
        cout<< get<0>(match_name)<<" "<< get<1>(match_name) <<endl;

    return 0;
}
