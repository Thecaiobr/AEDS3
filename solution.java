class solution {
    public boolean halvesAreAlike(String s) {
        int half = s.length()/2;
        int count1=0, count2 = 0;
        for (int i=0,j=half;i<half && j<s.length();i++,j++) {
            if(isVowel(s.charAt(i))){
                count1++;
            }
            if(isVowel(s.charAt(j))){
                count2++;
            }
        }
        if(count1==count2)
            return true;
        else
            return false;
    }
    
    public boolean isVowel (char c){
        c=Character.toLowerCase(c);
        if(c=='a'||c=='e'||c=='i'||c=='o'||c=='u')
            return true;
        else
            return false;
        
        
    }
    
}