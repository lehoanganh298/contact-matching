#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>
#include <fstream>
#include <tuple>
#include <map>
#include <locale>
#include <codecvt>

using namespace std;

std::map<wchar_t, tuple<int, int, int>> tone_map;

// convert UTF-8 wstring to wstring
std::wstring utf8_to_wstring(const std::string &str)
{
    std::wstring_convert<std::codecvt_utf8<wchar_t>> myconv;
    return myconv.from_bytes(str);
}

// convert wwstring to UTF-8 string
std::string wstring_to_utf8(const std::wstring &str)
{
    std::wstring_convert<std::codecvt_utf8<wchar_t>> myconv;
    return myconv.to_bytes(str);
}

bool contain(wstring s, wchar_t c)
{
    return s.find(c) != string::npos;
}

// vy --> vi, thy --> thi
// Rule: any 'y' at the end of word, follow a consonant
// Input: single word lower case
wstring y_to_i(wstring str)
{
    wstring phu_am = L"bcdđghklmnpqrstvx";
    wstring result = str;
    if (str.length() >= 2 && str[str.length() - 1] == 'y' && phu_am.find(str[str.length() - 2]) != string::npos)
    {
        result[str.length() - 1] = 'i';
    }
    return result;
}

// Lowercase a string
wstring lowercase(wstring s)
{
    wstring result = s;
    for (wchar_t &c : result)
    {
        c = towlower(c);
    }
    return result;
}

wstring normalize(wstring s)
{
    return y_to_i(lowercase(s));
}

// Split wstring into tokens seperated by delimiters. Empty tokens are ignored
vector<wstring> tokenize(wstring str, wstring delims = L" .()/|:;,")
{
    vector<wstring> tokens;
    wstring token = L"";

    for (wchar_t c : str)
    {
        if (contain(delims, c))
        {
            if (token.length() > 0)
            {
                tokens.push_back(token);
            }
            token = L"";
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

vector<wstring> tokenize_normalize(wstring s)
{
    vector<wstring> tokens = tokenize(s);
    for (wstring &token : tokens)
        token = normalize(token);
    return tokens;
}

float match_character(wchar_t c1, wchar_t c2)
{
    float score;
    if (tone_map.find(c1) == tone_map.end() or tone_map.find(c2) == tone_map.end())
    {
        if (c1 == c2)
            score = 1;
        else
            score = -1;
    }
    else
    {
        if (get<0>(tone_map[c1]) != get<0>(tone_map[c2]))
            score = -1;
        else
        {
            score = 0.3;
            if (get<1>(tone_map[c1]) == get<1>(tone_map[c2]))
                score += 0.4;
            if (get<2>(tone_map[c1]) == get<2>(tone_map[c2]))
                score += 0.3;
        }
    }
    // wcout<<c1<<L"-"<<c2<<L":"<<score<<endl;
    return score;
}

float match_token(wstring t1, wstring t2)
{
    if (t1.size() != t2.size())
        return -1;

    float sum_score = 0.0;
    for (int i = 0; i < t1.size(); i++)
    {
        float score = match_character(t1[i], t2[i]);
        if (score < 0)
            return -1;
        sum_score += score;
    }
    return sum_score / t1.size();
}

// TODO: sub has 2 same words
// TODO: order
float match_token_array(vector<wstring> v1, vector<wstring> v2)
{
    bool v2_contain_v1 = false;
    float sum_score = 0.0;

    for (wstring &token1 : v1)
    {
        bool has_token1 = false;

        for (wstring token2 : v2)
        {
            float match_score = match_token(token1, token2);
            if (match_score > 0)
            {
                has_token1 = true;
                sum_score += match_score;
            }
        }
        if (has_token1 == false)
            return -1;
    }
    return sum_score / v2.size();
}

vector<tuple<wstring, float>> match_contact(wstring query_name, const vector<wstring> &contact)
{
    vector<tuple<wstring, float>> result;
    vector<wstring> query_tokens = tokenize_normalize(query_name);

    for (wstring t : query_tokens)
    {
    }
    for (wstring contact_name : contact)
    {
        vector<wstring> contact_tokens = tokenize_normalize(contact_name);
        float match_score = match_token_array(query_tokens, contact_tokens);
        if (match_score > 0)
        {
            result.push_back(make_tuple(contact_name, match_score));
        }
    }
    return result;
}

int main()
{
    std::locale::global(std::locale(""));

    std::ifstream file_tone("tone");
    int line_count = 0;
    string s;

    while (getline(file_tone, s))
    {
        wstring ws = utf8_to_wstring(s);
        line_count += 1;
        for (int i = 0; i < ws.length() - 1; i++)
        {
            tone_map[ws[i]] = make_tuple(int(ws[ws.length() - 1] - '0'), line_count, i);
        }
    }

    std::ifstream file("contact");
    vector<wstring> contact;
    string line;
    while (getline(file, line))
    {
        wstring ws = utf8_to_wstring(line);
        contact.push_back(ws);
    }
    wcout << L"Enter name:";
    getline(cin, line);
    wstring query_name = utf8_to_wstring(line);
    for (auto match_name : match_contact(query_name, contact))
        wcout << get<0>(match_name) << " " << get<1>(match_name) << endl;
    return 0;
}
