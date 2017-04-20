#define __kernel
#define __global

int get_global_id(int i)
{
return 0;
}

__kernel void divNumbers(__global unsigned int* x,__global unsigned int* y,__global unsigned int* nq,__global unsigned int* nr)
{
   int numIndex = get_global_id(0);
   const int numLimbs = (2048 / 32);
   int base = numIndex * numLimbs;
   #pragma forceinline recursive
   {
      const int inlined_6_nr_m_size = numLimbs;
      unsigned int inlined_6_nr_m_base = base;
      const int inlined_6_nq_m_size = numLimbs;
      unsigned int inlined_6_nq_m_base = base;
      const int inlined_6_y_m_size = numLimbs;
      unsigned int inlined_6_y_m_base = base;
      const int inlined_6_x_m_size = numLimbs;
      unsigned int inlined_6_x_m_base = base;
      unsigned int inlined_6_ref_m_data[inlined_6_x_m_size];
      unsigned int inlined_6_ref_m_base = 0;
      const int inlined_6_ref_m_size = inlined_6_x_m_size;
      {
         int inlined_7_orig_m_size = inlined_6_x_m_size;
         unsigned int inlined_7_orig_m_base = inlined_6_x_m_base;
         int inlined_7_m_size = inlined_6_ref_m_size;
         unsigned int inlined_7_m_base = inlined_6_ref_m_base;
         int inlined_7_minCopySize = (inlined_7_m_size < inlined_7_orig_m_size)? inlined_7_m_size : inlined_7_orig_m_size;
         int inlined_7_i;
         for (inlined_7_i = 0;inlined_7_i < inlined_7_minCopySize;inlined_7_i++)
            inlined_6_ref_m_data[inlined_7_m_base + inlined_7_i] = x[inlined_7_orig_m_base + inlined_7_i];
         for ( ;inlined_7_i < inlined_7_m_size;inlined_7_i++)
            inlined_6_ref_m_data[inlined_7_m_base + inlined_7_i] = 0;
      }
      unsigned int inlined_6_divisor_m_data[inlined_6_y_m_size];
      unsigned int inlined_6_divisor_m_base = 0;
      const int inlined_6_divisor_m_size = inlined_6_y_m_size;
      {
         int inlined_8_orig_m_size = inlined_6_y_m_size;
         unsigned int inlined_8_orig_m_base = inlined_6_y_m_base;
         int inlined_8_m_size = inlined_6_divisor_m_size;
         unsigned int inlined_8_m_base = inlined_6_divisor_m_base;
         int inlined_8_minCopySize = (inlined_8_m_size < inlined_8_orig_m_size)? inlined_8_m_size : inlined_8_orig_m_size;
         int inlined_8_i;
         for (inlined_8_i = 0;inlined_8_i < inlined_8_minCopySize;inlined_8_i++)
            inlined_6_divisor_m_data[inlined_8_m_base + inlined_8_i] = y[inlined_8_orig_m_base + inlined_8_i];
         for ( ;inlined_8_i < inlined_8_m_size;inlined_8_i++)
            inlined_6_divisor_m_data[inlined_8_m_base + inlined_8_i] = 0;
      }
      unsigned int inlined_6_q_m_data[inlined_6_x_m_size];
      unsigned int inlined_6_q_m_base = 0;
      const int inlined_6_q_m_size = inlined_6_x_m_size;
      unsigned int inlined_6_r_m_data[inlined_6_x_m_size];
      unsigned int inlined_6_r_m_base = 0;
      const int inlined_6_r_m_size = inlined_6_x_m_size;
      {
         int inlined_9_m_size = inlined_6_q_m_size;
         unsigned int inlined_9_m_base = inlined_6_q_m_base;
         for (int inlined_9_i = 0;inlined_9_i < inlined_9_m_size;inlined_9_i++)
            inlined_6_q_m_data[inlined_9_m_base + inlined_9_i] = 0;
      }
      int inlined_ret_val_10;
      {
         int inlined_10_v_m_size = inlined_6_y_m_size;
         unsigned int inlined_10_v_m_base = inlined_6_y_m_base;
         int inlined_10_m_size = inlined_6_x_m_size;
         unsigned int inlined_10_m_base = inlined_6_x_m_base;
         int inlined_10_doLoop = 1;
         int inlined_10_ret = 0;
         for (int inlined_10_i = inlined_10_m_size - 1;((inlined_10_doLoop) && (inlined_10_i >= 0));inlined_10_i--)
         {
            if (inlined_10_i >= inlined_10_v_m_size)
            {
               if (x[inlined_10_m_base + inlined_10_i] != 0)
               {
                  inlined_10_ret = 0;
                  inlined_10_doLoop = 0;
               }
            }
            else
               if (x[inlined_10_m_base + inlined_10_i] < y[inlined_10_v_m_base + inlined_10_i])
               {
                  inlined_10_ret = 1;
                  inlined_10_doLoop = 0;
               }
               else
                  if (x[inlined_10_m_base + inlined_10_i] > y[inlined_10_v_m_base + inlined_10_i])
                  {
                     inlined_10_ret = 0;
                     inlined_10_doLoop = 0;
                  }
         }
         inlined_ret_val_10 = inlined_10_ret;
      }
      int inlined_6_ret = inlined_ret_val_10;
      if (inlined_6_ret)
      {
         {
            int inlined_11_orig_m_size = inlined_6_x_m_size;
            unsigned int inlined_11_orig_m_base = inlined_6_x_m_base;
            int inlined_11_m_size = inlined_6_nr_m_size;
            unsigned int inlined_11_m_base = inlined_6_nr_m_base;
            int inlined_11_minCopySize = (inlined_11_m_size < inlined_11_orig_m_size)? inlined_11_m_size : inlined_11_orig_m_size;
            int inlined_11_i;
            for (inlined_11_i = 0;inlined_11_i < inlined_11_minCopySize;inlined_11_i++)
               nr[inlined_11_m_base + inlined_11_i] = x[inlined_11_orig_m_base + inlined_11_i];
            for ( ;inlined_11_i < inlined_11_m_size;inlined_11_i++)
               nr[inlined_11_m_base + inlined_11_i] = 0;
         }
         {
            int inlined_12_orig_m_size = inlined_6_q_m_size;
            unsigned int inlined_12_orig_m_base = inlined_6_q_m_base;
            int inlined_12_m_size = inlined_6_nq_m_size;
            unsigned int inlined_12_m_base = inlined_6_nq_m_base;
            int inlined_12_minCopySize = (inlined_12_m_size < inlined_12_orig_m_size)? inlined_12_m_size : inlined_12_orig_m_size;
            int inlined_12_i;
            for (inlined_12_i = 0;inlined_12_i < inlined_12_minCopySize;inlined_12_i++)
               nq[inlined_12_m_base + inlined_12_i] = inlined_6_q_m_data[inlined_12_orig_m_base + inlined_12_i];
            for ( ;inlined_12_i < inlined_12_m_size;inlined_12_i++)
               nq[inlined_12_m_base + inlined_12_i] = 0;
         }
      }
      else
{
         int inlined_ret_val_13;
         {
            int inlined_13_m_size = inlined_6_y_m_size;
            unsigned int inlined_13_m_base = inlined_6_y_m_base;
            int inlined_13_len = 0;
            int inlined_13_greaterActiveLimb = 0;
            for (int inlined_13_i = inlined_13_m_size - 1;((inlined_13_i > 0) && (inlined_13_greaterActiveLimb == 0));inlined_13_i--)
            {
               if (y[inlined_13_m_base + inlined_13_i] > 0)
                  inlined_13_greaterActiveLimb = inlined_13_i;
            }
            unsigned int inlined_13_test = y[inlined_13_m_base + inlined_13_greaterActiveLimb];
            int inlined_13_numActiveBits = 0;
            while (inlined_13_test > 0)
            {
               inlined_13_test = inlined_13_test >> 1;
               inlined_13_numActiveBits++;
            }
            inlined_13_len = (inlined_13_numActiveBits + inlined_13_greaterActiveLimb * 32);
            inlined_ret_val_13 = inlined_13_len;
         }
         int inlined_6_yl = inlined_ret_val_13;
         int inlined_ret_val_14;
         {
            int inlined_14_m_size = inlined_6_ref_m_size;
            unsigned int inlined_14_m_base = inlined_6_ref_m_base;
            int inlined_14_len = 0;
            int inlined_14_greaterActiveLimb = 0;
            for (int inlined_14_i = inlined_14_m_size - 1;((inlined_14_i > 0) && (inlined_14_greaterActiveLimb == 0));inlined_14_i--)
            {
               if (inlined_6_ref_m_data[inlined_14_m_base + inlined_14_i] > 0)
                  inlined_14_greaterActiveLimb = inlined_14_i;
            }
            unsigned int inlined_14_test = inlined_6_ref_m_data[inlined_14_m_base + inlined_14_greaterActiveLimb];
            int inlined_14_numActiveBits = 0;
            while (inlined_14_test > 0)
            {
               inlined_14_test = inlined_14_test >> 1;
               inlined_14_numActiveBits++;
            }
            inlined_14_len = (inlined_14_numActiveBits + inlined_14_greaterActiveLimb * 32);
            inlined_ret_val_14 = inlined_14_len;
         }
         int inlined_6_rl = inlined_ret_val_14;
         int inlined_6_downBit = inlined_6_rl - inlined_6_yl;
         {
            int inlined_15_orig_m_size = inlined_6_ref_m_size;
            unsigned int inlined_15_orig_m_base = inlined_6_ref_m_base;
            int inlined_15_m_size = inlined_6_r_m_size;
            unsigned int inlined_15_m_base = inlined_6_r_m_base;
            int inlined_15_minCopySize = (inlined_15_m_size < inlined_15_orig_m_size)? inlined_15_m_size : inlined_15_orig_m_size;
            int inlined_15_i;
            for (inlined_15_i = 0;inlined_15_i < inlined_15_minCopySize;inlined_15_i++)
               inlined_6_r_m_data[inlined_15_m_base + inlined_15_i] = inlined_6_ref_m_data[inlined_15_orig_m_base + inlined_15_i];
            for ( ;inlined_15_i < inlined_15_m_size;inlined_15_i++)
               inlined_6_r_m_data[inlined_15_m_base + inlined_15_i] = 0;
         }
         {
            int inlined_16_down = inlined_6_downBit;
            int inlined_16_up = inlined_6_rl - 1;
            const int inlined_16_m_size = inlined_6_r_m_size;
            unsigned int inlined_16_m_base = inlined_6_r_m_base;
            unsigned int inlined_16_ref_m_data[inlined_16_m_size];
            unsigned int inlined_16_ref_m_base = inlined_16_m_base;
            int inlined_16_ref_m_size = inlined_16_m_size;
            {
               int inlined_17_orig_m_size = inlined_16_m_size;
               unsigned int inlined_17_orig_m_base = inlined_16_m_base;
               int inlined_17_m_size = inlined_16_ref_m_size;
               unsigned int inlined_17_m_base = inlined_16_ref_m_base;
               int inlined_17_minCopySize = (inlined_17_m_size < inlined_17_orig_m_size)? inlined_17_m_size : inlined_17_orig_m_size;
               int inlined_17_i;
               for (inlined_17_i = 0;inlined_17_i < inlined_17_minCopySize;inlined_17_i++)
                  inlined_16_ref_m_data[inlined_17_m_base + inlined_17_i] = inlined_6_r_m_data[inlined_17_orig_m_base + inlined_17_i];
               for ( ;inlined_17_i < inlined_17_m_size;inlined_17_i++)
                  inlined_16_ref_m_data[inlined_17_m_base + inlined_17_i] = 0;
            }
            {
               int inlined_18_lower = inlined_16_down;
               int inlined_18_upper = inlined_16_up;
               const int inlined_18_x_m_size = inlined_16_ref_m_size;
               unsigned int inlined_18_x_m_base = inlined_16_ref_m_base;
               const int inlined_18_r_m_size = inlined_16_m_size;
               unsigned int inlined_18_r_m_base = inlined_16_m_base;
               int inlined_ret_val_19;
               {
                  int inlined_19_m_size = inlined_18_x_m_size;
                  unsigned int inlined_19_m_base = inlined_18_x_m_base;
                  inlined_ret_val_19 = inlined_19_m_size * 32;
               }
               int inlined_18_zl = inlined_ret_val_19;
               inlined_18_zl = inlined_18_zl - 1 - (inlined_18_upper + 1);
               {
                  int inlined_20_sv = inlined_18_zl;
                  int inlined_20_a_m_size = inlined_18_x_m_size;
                  unsigned int inlined_20_a_m_base = inlined_18_x_m_base;
                  int inlined_20_r_m_size = inlined_18_r_m_size;
                  unsigned int inlined_20_r_m_base = inlined_18_r_m_base;
                  int inlined_20_i;
                  unsigned int inlined_20_carry = 0;
                  int inlined_20_limbsShifted = inlined_20_sv / 32;
                  if (inlined_20_limbsShifted)
                  {
                     int inlined_20_slots = inlined_20_limbsShifted;
                     for (inlined_20_i = inlined_20_r_m_size - 1;inlined_20_i >= inlined_20_slots;inlined_20_i--)
                     {
                        inlined_6_r_m_data[inlined_20_r_m_base + inlined_20_i] = inlined_16_ref_m_data[inlined_20_a_m_base + inlined_20_i - inlined_20_slots];
                     }
                     for (inlined_20_i = inlined_20_slots - 1;inlined_20_i >= 0;inlined_20_i--)
                        inlined_6_r_m_data[inlined_20_r_m_base + inlined_20_i] = 0;
                     inlined_20_sv -= inlined_20_limbsShifted * 32;
                  }
                  else
                     if (inlined_6_r_m_data != inlined_16_ref_m_data)
                     {
                        {
                           int inlined_21_orig_m_size = inlined_20_a_m_size;
                           unsigned int inlined_21_orig_m_base = inlined_20_a_m_base;
                           int inlined_21_m_size = inlined_20_r_m_size;
                           unsigned int inlined_21_m_base = inlined_20_r_m_base;
                           int inlined_21_minCopySize = (inlined_21_m_size < inlined_21_orig_m_size)? inlined_21_m_size : inlined_21_orig_m_size;
                           int inlined_21_i;
                           for (inlined_21_i = 0;inlined_21_i < inlined_21_minCopySize;inlined_21_i++)
                              inlined_6_r_m_data[inlined_21_m_base + inlined_21_i] = inlined_16_ref_m_data[inlined_21_orig_m_base + inlined_21_i];
                           for ( ;inlined_21_i < inlined_21_m_size;inlined_21_i++)
                              inlined_6_r_m_data[inlined_21_m_base + inlined_21_i] = 0;
                        }
                     }
                  int inlined_20_cs = 32 - inlined_20_sv;
                  for (inlined_20_i = 0;inlined_20_i < inlined_20_r_m_size;inlined_20_i++)
                  {
                     int inlined_20_nv = (inlined_6_r_m_data[inlined_20_r_m_base + inlined_20_i] << inlined_20_sv) | inlined_20_carry;
                     inlined_20_carry = ~((0xFFFFFFFF << inlined_20_sv) >> inlined_20_sv);
                     inlined_20_carry &= inlined_6_r_m_data[inlined_20_r_m_base + inlined_20_i];
                     inlined_20_carry = inlined_20_carry >> inlined_20_cs;
                     inlined_6_r_m_data[inlined_20_r_m_base + inlined_20_i] = inlined_20_nv;
                  }
               }
               {
                  int inlined_22_sv = inlined_18_zl + inlined_18_lower;
                  int inlined_22_a_m_size = inlined_18_r_m_size;
                  unsigned int inlined_22_a_m_base = inlined_18_r_m_base;
                  int inlined_22_r_m_size = inlined_18_r_m_size;
                  unsigned int inlined_22_r_m_base = inlined_18_r_m_base;
                  unsigned int inlined_22_carry = 0;
                  unsigned int inlined_22_limbsShifted = inlined_22_sv / 32;
                  if (inlined_22_limbsShifted)
                  {
                     int inlined_22_i;
                     int inlined_22_slots = inlined_22_sv / 32;
                     unsigned int inlined_22_val;
                     for (inlined_22_i = 0;inlined_22_i < inlined_22_r_m_size - inlined_22_slots;inlined_22_i++)
                     {
                        inlined_22_val = (inlined_22_i + inlined_22_slots < inlined_22_a_m_size)? inlined_6_r_m_data[inlined_22_a_m_base + inlined_22_i + inlined_22_slots] : 0;
                        inlined_6_r_m_data[inlined_22_r_m_base + inlined_22_i] = inlined_22_val;
                     }
                     for (inlined_22_i = inlined_22_slots;inlined_22_i > 0;inlined_22_i--)
                        inlined_6_r_m_data[inlined_22_r_m_base + inlined_22_r_m_size - inlined_22_i] = 0;
                     inlined_22_sv -= inlined_22_limbsShifted * 32;
                  }
                  else
                     if (inlined_6_r_m_data != inlined_6_r_m_data)
                     {
                        {
                           int inlined_23_orig_m_size = inlined_22_a_m_size;
                           unsigned int inlined_23_orig_m_base = inlined_22_a_m_base;
                           int inlined_23_m_size = inlined_22_r_m_size;
                           unsigned int inlined_23_m_base = inlined_22_r_m_base;
                           int inlined_23_minCopySize = (inlined_23_m_size < inlined_23_orig_m_size)? inlined_23_m_size : inlined_23_orig_m_size;
                           int inlined_23_i;
                           for (inlined_23_i = 0;inlined_23_i < inlined_23_minCopySize;inlined_23_i++)
                              inlined_6_r_m_data[inlined_23_m_base + inlined_23_i] = inlined_6_r_m_data[inlined_23_orig_m_base + inlined_23_i];
                           for ( ;inlined_23_i < inlined_23_m_size;inlined_23_i++)
                              inlined_6_r_m_data[inlined_23_m_base + inlined_23_i] = 0;
                        }
                     }
                  int inlined_22_cs = 32 - inlined_22_sv;
                  for (int inlined_22_i = inlined_22_r_m_size - 1;inlined_22_i >= 0;inlined_22_i--)
                  {
                     unsigned int inlined_22_nv = (inlined_6_r_m_data[inlined_22_r_m_base + inlined_22_i] >> inlined_22_sv) | inlined_22_carry;
                     inlined_22_carry = ~((0xFFFFFFFF >> inlined_22_sv) << inlined_22_sv);
                     inlined_22_carry &= inlined_6_r_m_data[inlined_22_r_m_base + inlined_22_i];
                     inlined_22_carry = inlined_22_carry << inlined_22_cs;
                     inlined_6_r_m_data[inlined_22_r_m_base + inlined_22_i] = inlined_22_nv;
                  }
               }
            }
         }
         inlined_6_downBit--;
         int inlined_ret_val_24;
         {
            int inlined_24_v_m_size = inlined_6_divisor_m_size;
            unsigned int inlined_24_v_m_base = inlined_6_divisor_m_base;
            int inlined_24_m_size = inlined_6_r_m_size;
            unsigned int inlined_24_m_base = inlined_6_r_m_base;
            int inlined_24_doLoop = 1;
            int inlined_24_ret = 0;
            for (int inlined_24_i = inlined_24_m_size - 1;((inlined_24_doLoop) && (inlined_24_i >= 0));inlined_24_i--)
            {
               if (inlined_24_i >= inlined_24_v_m_size)
               {
                  if (inlined_6_r_m_data[inlined_24_m_base + inlined_24_i] != 0)
                  {
                     inlined_24_ret = 0;
                     inlined_24_doLoop = 0;
                  }
               }
               else
                  if (inlined_6_r_m_data[inlined_24_m_base + inlined_24_i] < inlined_6_divisor_m_data[inlined_24_v_m_base + inlined_24_i])
                  {
                     inlined_24_ret = 1;
                     inlined_24_doLoop = 0;
                  }
                  else
                     if (inlined_6_r_m_data[inlined_24_m_base + inlined_24_i] > inlined_6_divisor_m_data[inlined_24_v_m_base + inlined_24_i])
                     {
                        inlined_24_ret = 0;
                        inlined_24_doLoop = 0;
                     }
            }
            inlined_ret_val_24 = inlined_24_ret;
         }
         inlined_6_ret = inlined_ret_val_24;
         if (inlined_6_ret)
         {
            {
               int inlined_25_orig_m_size = inlined_6_ref_m_size;
               unsigned int inlined_25_orig_m_base = inlined_6_ref_m_base;
               int inlined_25_m_size = inlined_6_r_m_size;
               unsigned int inlined_25_m_base = inlined_6_r_m_base;
               int inlined_25_minCopySize = (inlined_25_m_size < inlined_25_orig_m_size)? inlined_25_m_size : inlined_25_orig_m_size;
               int inlined_25_i;
               for (inlined_25_i = 0;inlined_25_i < inlined_25_minCopySize;inlined_25_i++)
                  inlined_6_r_m_data[inlined_25_m_base + inlined_25_i] = inlined_6_ref_m_data[inlined_25_orig_m_base + inlined_25_i];
               for ( ;inlined_25_i < inlined_25_m_size;inlined_25_i++)
                  inlined_6_r_m_data[inlined_25_m_base + inlined_25_i] = 0;
            }
            {
               int inlined_26_down = inlined_6_downBit;
               int inlined_26_up = inlined_6_rl - 1;
               const int inlined_26_m_size = inlined_6_r_m_size;
               unsigned int inlined_26_m_base = inlined_6_r_m_base;
               unsigned int inlined_26_ref_m_data[inlined_26_m_size];
               unsigned int inlined_26_ref_m_base = inlined_26_m_base;
               int inlined_26_ref_m_size = inlined_26_m_size;
               {
                  int inlined_27_orig_m_size = inlined_26_m_size;
                  unsigned int inlined_27_orig_m_base = inlined_26_m_base;
                  int inlined_27_m_size = inlined_26_ref_m_size;
                  unsigned int inlined_27_m_base = inlined_26_ref_m_base;
                  int inlined_27_minCopySize = (inlined_27_m_size < inlined_27_orig_m_size)? inlined_27_m_size : inlined_27_orig_m_size;
                  int inlined_27_i;
                  for (inlined_27_i = 0;inlined_27_i < inlined_27_minCopySize;inlined_27_i++)
                     inlined_26_ref_m_data[inlined_27_m_base + inlined_27_i] = inlined_6_r_m_data[inlined_27_orig_m_base + inlined_27_i];
                  for ( ;inlined_27_i < inlined_27_m_size;inlined_27_i++)
                     inlined_26_ref_m_data[inlined_27_m_base + inlined_27_i] = 0;
               }
               {
                  int inlined_28_lower = inlined_26_down;
                  int inlined_28_upper = inlined_26_up;
                  const int inlined_28_x_m_size = inlined_26_ref_m_size;
                  unsigned int inlined_28_x_m_base = inlined_26_ref_m_base;
                  const int inlined_28_r_m_size = inlined_26_m_size;
                  unsigned int inlined_28_r_m_base = inlined_26_m_base;
                  int inlined_ret_val_29;
                  {
                     int inlined_29_m_size = inlined_28_x_m_size;
                     unsigned int inlined_29_m_base = inlined_28_x_m_base;
                     inlined_ret_val_29 = inlined_29_m_size * 32;
                  }
                  int inlined_28_zl = inlined_ret_val_29;
                  inlined_28_zl = inlined_28_zl - 1 - (inlined_28_upper + 1);
                  {
                     int inlined_30_sv = inlined_28_zl;
                     int inlined_30_a_m_size = inlined_28_x_m_size;
                     unsigned int inlined_30_a_m_base = inlined_28_x_m_base;
                     int inlined_30_r_m_size = inlined_28_r_m_size;
                     unsigned int inlined_30_r_m_base = inlined_28_r_m_base;
                     int inlined_30_i;
                     unsigned int inlined_30_carry = 0;
                     int inlined_30_limbsShifted = inlined_30_sv / 32;
                     if (inlined_30_limbsShifted)
                     {
                        int inlined_30_slots = inlined_30_limbsShifted;
                        for (inlined_30_i = inlined_30_r_m_size - 1;inlined_30_i >= inlined_30_slots;inlined_30_i--)
                        {
                           inlined_6_r_m_data[inlined_30_r_m_base + inlined_30_i] = inlined_26_ref_m_data[inlined_30_a_m_base + inlined_30_i - inlined_30_slots];
                        }
                        for (inlined_30_i = inlined_30_slots - 1;inlined_30_i >= 0;inlined_30_i--)
                           inlined_6_r_m_data[inlined_30_r_m_base + inlined_30_i] = 0;
                        inlined_30_sv -= inlined_30_limbsShifted * 32;
                     }
                     else
                        if (inlined_6_r_m_data != inlined_26_ref_m_data)
                        {
                           {
                              int inlined_31_orig_m_size = inlined_30_a_m_size;
                              unsigned int inlined_31_orig_m_base = inlined_30_a_m_base;
                              int inlined_31_m_size = inlined_30_r_m_size;
                              unsigned int inlined_31_m_base = inlined_30_r_m_base;
                              int inlined_31_minCopySize = (inlined_31_m_size < inlined_31_orig_m_size)? inlined_31_m_size : inlined_31_orig_m_size;
                              int inlined_31_i;
                              for (inlined_31_i = 0;inlined_31_i < inlined_31_minCopySize;inlined_31_i++)
                                 inlined_6_r_m_data[inlined_31_m_base + inlined_31_i] = inlined_26_ref_m_data[inlined_31_orig_m_base + inlined_31_i];
                              for ( ;inlined_31_i < inlined_31_m_size;inlined_31_i++)
                                 inlined_6_r_m_data[inlined_31_m_base + inlined_31_i] = 0;
                           }
                        }
                     int inlined_30_cs = 32 - inlined_30_sv;
                     for (inlined_30_i = 0;inlined_30_i < inlined_30_r_m_size;inlined_30_i++)
                     {
                        int inlined_30_nv = (inlined_6_r_m_data[inlined_30_r_m_base + inlined_30_i] << inlined_30_sv) | inlined_30_carry;
                        inlined_30_carry = ~((0xFFFFFFFF << inlined_30_sv) >> inlined_30_sv);
                        inlined_30_carry &= inlined_6_r_m_data[inlined_30_r_m_base + inlined_30_i];
                        inlined_30_carry = inlined_30_carry >> inlined_30_cs;
                        inlined_6_r_m_data[inlined_30_r_m_base + inlined_30_i] = inlined_30_nv;
                     }
                  }
                  {
                     int inlined_32_sv = inlined_28_zl + inlined_28_lower;
                     int inlined_32_a_m_size = inlined_28_r_m_size;
                     unsigned int inlined_32_a_m_base = inlined_28_r_m_base;
                     int inlined_32_r_m_size = inlined_28_r_m_size;
                     unsigned int inlined_32_r_m_base = inlined_28_r_m_base;
                     unsigned int inlined_32_carry = 0;
                     unsigned int inlined_32_limbsShifted = inlined_32_sv / 32;
                     if (inlined_32_limbsShifted)
                     {
                        int inlined_32_i;
                        int inlined_32_slots = inlined_32_sv / 32;
                        unsigned int inlined_32_val;
                        for (inlined_32_i = 0;inlined_32_i < inlined_32_r_m_size - inlined_32_slots;inlined_32_i++)
                        {
                           inlined_32_val = (inlined_32_i + inlined_32_slots < inlined_32_a_m_size)? inlined_6_r_m_data[inlined_32_a_m_base + inlined_32_i + inlined_32_slots] : 0;
                           inlined_6_r_m_data[inlined_32_r_m_base + inlined_32_i] = inlined_32_val;
                        }
                        for (inlined_32_i = inlined_32_slots;inlined_32_i > 0;inlined_32_i--)
                           inlined_6_r_m_data[inlined_32_r_m_base + inlined_32_r_m_size - inlined_32_i] = 0;
                        inlined_32_sv -= inlined_32_limbsShifted * 32;
                     }
                     else
                        if (inlined_6_r_m_data != inlined_6_r_m_data)
                        {
                           {
                              int inlined_33_orig_m_size = inlined_32_a_m_size;
                              unsigned int inlined_33_orig_m_base = inlined_32_a_m_base;
                              int inlined_33_m_size = inlined_32_r_m_size;
                              unsigned int inlined_33_m_base = inlined_32_r_m_base;
                              int inlined_33_minCopySize = (inlined_33_m_size < inlined_33_orig_m_size)? inlined_33_m_size : inlined_33_orig_m_size;
                              int inlined_33_i;
                              for (inlined_33_i = 0;inlined_33_i < inlined_33_minCopySize;inlined_33_i++)
                                 inlined_6_r_m_data[inlined_33_m_base + inlined_33_i] = inlined_6_r_m_data[inlined_33_orig_m_base + inlined_33_i];
                              for ( ;inlined_33_i < inlined_33_m_size;inlined_33_i++)
                                 inlined_6_r_m_data[inlined_33_m_base + inlined_33_i] = 0;
                           }
                        }
                     int inlined_32_cs = 32 - inlined_32_sv;
                     for (int inlined_32_i = inlined_32_r_m_size - 1;inlined_32_i >= 0;inlined_32_i--)
                     {
                        unsigned int inlined_32_nv = (inlined_6_r_m_data[inlined_32_r_m_base + inlined_32_i] >> inlined_32_sv) | inlined_32_carry;
                        inlined_32_carry = ~((0xFFFFFFFF >> inlined_32_sv) << inlined_32_sv);
                        inlined_32_carry &= inlined_6_r_m_data[inlined_32_r_m_base + inlined_32_i];
                        inlined_32_carry = inlined_32_carry << inlined_32_cs;
                        inlined_6_r_m_data[inlined_32_r_m_base + inlined_32_i] = inlined_32_nv;
                     }
                  }
               }
            }
            inlined_6_downBit--;
         }
         {
            int inlined_34_m_size = inlined_6_q_m_size;
            unsigned int inlined_34_m_base = inlined_6_q_m_base;
            int inlined_34_i = 0;
            int inlined_34_doRun = 1;
            while (inlined_34_doRun)
            {
               inlined_6_q_m_data[inlined_34_m_base + inlined_34_i]++;
               inlined_34_doRun = (inlined_6_q_m_data[inlined_34_m_base + inlined_34_i] == 0);
               inlined_34_i++;
            }
         }
         {
            int inlined_35_y_m_size = inlined_6_divisor_m_size;
            unsigned int inlined_35_y_m_base = inlined_6_divisor_m_base;
            int inlined_35_m_size = inlined_6_r_m_size;
            unsigned int inlined_35_m_base = inlined_6_r_m_base;
            unsigned int inlined_35_ref_m_data[inlined_35_m_size];
            unsigned int inlined_35_ref_m_base = 0;
            int inlined_35_ref_m_size = inlined_35_m_size;
            {
               int inlined_36_orig_m_size = inlined_35_m_size;
               unsigned int inlined_36_orig_m_base = inlined_35_m_base;
               int inlined_36_m_size = inlined_35_ref_m_size;
               unsigned int inlined_36_m_base = inlined_35_ref_m_base;
               int inlined_36_minCopySize = (inlined_36_m_size < inlined_36_orig_m_size)? inlined_36_m_size : inlined_36_orig_m_size;
               int inlined_36_i;
               for (inlined_36_i = 0;inlined_36_i < inlined_36_minCopySize;inlined_36_i++)
                  inlined_35_ref_m_data[inlined_36_m_base + inlined_36_i] = inlined_6_r_m_data[inlined_36_orig_m_base + inlined_36_i];
               for ( ;inlined_36_i < inlined_36_m_size;inlined_36_i++)
                  inlined_35_ref_m_data[inlined_36_m_base + inlined_36_i] = 0;
            }
            {
               int inlined_37_y_m_size = inlined_35_y_m_size;
               unsigned int inlined_37_y_m_base = inlined_35_y_m_base;
               int inlined_37_x_m_size = inlined_35_ref_m_size;
               unsigned int inlined_37_x_m_base = inlined_35_ref_m_base;
               int inlined_37_r_m_size = inlined_35_m_size;
               unsigned int inlined_37_r_m_base = inlined_35_m_base;
               unsigned int inlined_37_carryIn = 0;
               unsigned int inlined_37_carryOut = 0;
               for (int inlined_37_i = 0;inlined_37_i < inlined_37_x_m_size;inlined_37_i++)
               {
                  unsigned int inlined_37_sum = 0;
                  if (inlined_37_i < inlined_37_x_m_size)
                     inlined_37_sum += inlined_35_ref_m_data[inlined_37_x_m_base + inlined_37_i];
                  if (inlined_37_i < inlined_37_y_m_size)
                     inlined_37_sum -= inlined_6_divisor_m_data[inlined_37_y_m_base + inlined_37_i];
                  if (inlined_37_sum > inlined_35_ref_m_data[inlined_37_x_m_base + inlined_37_i])
                  {
                     if (inlined_37_carryIn)
                        inlined_37_sum--;
                     inlined_37_carryOut = 1;
                  }
                  else
                     if (inlined_37_carryIn)
                     {
                        inlined_37_carryOut = (inlined_37_sum == 0)? 1 : 0;
                        inlined_37_sum--;
                     }
                  inlined_37_carryIn = inlined_37_carryOut;
                  inlined_6_r_m_data[inlined_37_r_m_base + inlined_37_i] = inlined_37_sum;
               }
            }
         }
         while (inlined_6_downBit >= 0)
         {
            {
               int inlined_38_bits = 1;
               int inlined_38_m_size = inlined_6_r_m_size;
               unsigned int inlined_38_m_base = inlined_6_r_m_base;
               unsigned int inlined_38_ref_m_data[inlined_38_m_size];
               unsigned int inlined_38_ref_m_base = inlined_38_m_base;
               int inlined_38_ref_m_size = inlined_38_m_size;
               {
                  int inlined_39_orig_m_size = inlined_38_m_size;
                  unsigned int inlined_39_orig_m_base = inlined_38_m_base;
                  int inlined_39_m_size = inlined_38_ref_m_size;
                  unsigned int inlined_39_m_base = inlined_38_ref_m_base;
                  int inlined_39_minCopySize = (inlined_39_m_size < inlined_39_orig_m_size)? inlined_39_m_size : inlined_39_orig_m_size;
                  int inlined_39_i;
                  for (inlined_39_i = 0;inlined_39_i < inlined_39_minCopySize;inlined_39_i++)
                     inlined_38_ref_m_data[inlined_39_m_base + inlined_39_i] = inlined_6_r_m_data[inlined_39_orig_m_base + inlined_39_i];
                  for ( ;inlined_39_i < inlined_39_m_size;inlined_39_i++)
                     inlined_38_ref_m_data[inlined_39_m_base + inlined_39_i] = 0;
               }
               {
                  int inlined_40_sv = inlined_38_bits;
                  int inlined_40_a_m_size = inlined_38_ref_m_size;
                  unsigned int inlined_40_a_m_base = inlined_38_ref_m_base;
                  int inlined_40_r_m_size = inlined_38_m_size;
                  unsigned int inlined_40_r_m_base = inlined_38_m_base;
                  int inlined_40_i;
                  unsigned int inlined_40_carry = 0;
                  int inlined_40_limbsShifted = inlined_40_sv / 32;
                  if (inlined_40_limbsShifted)
                  {
                     int inlined_40_slots = inlined_40_limbsShifted;
                     for (inlined_40_i = inlined_40_r_m_size - 1;inlined_40_i >= inlined_40_slots;inlined_40_i--)
                     {
                        inlined_6_r_m_data[inlined_40_r_m_base + inlined_40_i] = inlined_38_ref_m_data[inlined_40_a_m_base + inlined_40_i - inlined_40_slots];
                     }
                     for (inlined_40_i = inlined_40_slots - 1;inlined_40_i >= 0;inlined_40_i--)
                        inlined_6_r_m_data[inlined_40_r_m_base + inlined_40_i] = 0;
                     inlined_40_sv -= inlined_40_limbsShifted * 32;
                  }
                  else
                     if (inlined_6_r_m_data != inlined_38_ref_m_data)
                     {
                        {
                           int inlined_41_orig_m_size = inlined_40_a_m_size;
                           unsigned int inlined_41_orig_m_base = inlined_40_a_m_base;
                           int inlined_41_m_size = inlined_40_r_m_size;
                           unsigned int inlined_41_m_base = inlined_40_r_m_base;
                           int inlined_41_minCopySize = (inlined_41_m_size < inlined_41_orig_m_size)? inlined_41_m_size : inlined_41_orig_m_size;
                           int inlined_41_i;
                           for (inlined_41_i = 0;inlined_41_i < inlined_41_minCopySize;inlined_41_i++)
                              inlined_6_r_m_data[inlined_41_m_base + inlined_41_i] = inlined_38_ref_m_data[inlined_41_orig_m_base + inlined_41_i];
                           for ( ;inlined_41_i < inlined_41_m_size;inlined_41_i++)
                              inlined_6_r_m_data[inlined_41_m_base + inlined_41_i] = 0;
                        }
                     }
                  int inlined_40_cs = 32 - inlined_40_sv;
                  for (inlined_40_i = 0;inlined_40_i < inlined_40_r_m_size;inlined_40_i++)
                  {
                     int inlined_40_nv = (inlined_6_r_m_data[inlined_40_r_m_base + inlined_40_i] << inlined_40_sv) | inlined_40_carry;
                     inlined_40_carry = ~((0xFFFFFFFF << inlined_40_sv) >> inlined_40_sv);
                     inlined_40_carry &= inlined_6_r_m_data[inlined_40_r_m_base + inlined_40_i];
                     inlined_40_carry = inlined_40_carry >> inlined_40_cs;
                     inlined_6_r_m_data[inlined_40_r_m_base + inlined_40_i] = inlined_40_nv;
                  }
               }
            }
            int inlined_ret_val_42;
            {
               int inlined_42_bitnum = inlined_6_downBit;
               int inlined_42_m_size = inlined_6_ref_m_size;
               unsigned int inlined_42_m_base = inlined_6_ref_m_base;
               int inlined_42_limbIndex = inlined_42_bitnum / 32;
               int inlined_42_bitIndex = inlined_42_bitnum % 32;
               inlined_ret_val_42 = (inlined_6_ref_m_data[inlined_42_m_base + inlined_42_limbIndex] >> inlined_42_bitIndex) & 1;
            }
            inlined_6_ret = inlined_ret_val_42;
            inlined_6_downBit--;
            if (inlined_6_ret)
            {
               int inlined_43_m_size = inlined_6_r_m_size;
               unsigned int inlined_43_m_base = inlined_6_r_m_base;
               int inlined_43_i = 0;
               int inlined_43_doRun = 1;
               while (inlined_43_doRun)
               {
                  inlined_6_r_m_data[inlined_43_m_base + inlined_43_i]++;
                  inlined_43_doRun = (inlined_6_r_m_data[inlined_43_m_base + inlined_43_i] == 0);
                  inlined_43_i++;
               }
            }
            int inlined_ret_val_44;
            {
               int inlined_44_v_m_size = inlined_6_divisor_m_size;
               unsigned int inlined_44_v_m_base = inlined_6_divisor_m_base;
               int inlined_44_m_size = inlined_6_r_m_size;
               unsigned int inlined_44_m_base = inlined_6_r_m_base;
               int inlined_44_doLoop = 1;
               int inlined_44_ret = 0;
               for (int inlined_44_i = inlined_44_m_size - 1;((inlined_44_doLoop) && (inlined_44_i >= 0));inlined_44_i--)
               {
                  if (inlined_44_i >= inlined_44_v_m_size)
                  {
                     if (inlined_6_r_m_data[inlined_44_m_base + inlined_44_i] != 0)
                     {
                        inlined_44_ret = 0;
                        inlined_44_doLoop = 0;
                     }
                  }
                  else
                     if (inlined_6_r_m_data[inlined_44_m_base + inlined_44_i] < inlined_6_divisor_m_data[inlined_44_v_m_base + inlined_44_i])
                     {
                        inlined_44_ret = 1;
                        inlined_44_doLoop = 0;
                     }
                     else
                        if (inlined_6_r_m_data[inlined_44_m_base + inlined_44_i] > inlined_6_divisor_m_data[inlined_44_v_m_base + inlined_44_i])
                        {
                           inlined_44_ret = 0;
                           inlined_44_doLoop = 0;
                        }
               }
               inlined_ret_val_44 = inlined_44_ret;
            }
            inlined_6_ret = inlined_ret_val_44;
            if (inlined_6_ret)
            {
               {
                  int inlined_45_bits = 1;
                  int inlined_45_m_size = inlined_6_q_m_size;
                  unsigned int inlined_45_m_base = inlined_6_q_m_base;
                  unsigned int inlined_45_ref_m_data[inlined_45_m_size];
                  unsigned int inlined_45_ref_m_base = inlined_45_m_base;
                  int inlined_45_ref_m_size = inlined_45_m_size;
                  {
                     int inlined_46_orig_m_size = inlined_45_m_size;
                     unsigned int inlined_46_orig_m_base = inlined_45_m_base;
                     int inlined_46_m_size = inlined_45_ref_m_size;
                     unsigned int inlined_46_m_base = inlined_45_ref_m_base;
                     int inlined_46_minCopySize = (inlined_46_m_size < inlined_46_orig_m_size)? inlined_46_m_size : inlined_46_orig_m_size;
                     int inlined_46_i;
                     for (inlined_46_i = 0;inlined_46_i < inlined_46_minCopySize;inlined_46_i++)
                        inlined_45_ref_m_data[inlined_46_m_base + inlined_46_i] = inlined_6_q_m_data[inlined_46_orig_m_base + inlined_46_i];
                     for ( ;inlined_46_i < inlined_46_m_size;inlined_46_i++)
                        inlined_45_ref_m_data[inlined_46_m_base + inlined_46_i] = 0;
                  }
                  {
                     int inlined_47_sv = inlined_45_bits;
                     int inlined_47_a_m_size = inlined_45_ref_m_size;
                     unsigned int inlined_47_a_m_base = inlined_45_ref_m_base;
                     int inlined_47_r_m_size = inlined_45_m_size;
                     unsigned int inlined_47_r_m_base = inlined_45_m_base;
                     int inlined_47_i;
                     unsigned int inlined_47_carry = 0;
                     int inlined_47_limbsShifted = inlined_47_sv / 32;
                     if (inlined_47_limbsShifted)
                     {
                        int inlined_47_slots = inlined_47_limbsShifted;
                        for (inlined_47_i = inlined_47_r_m_size - 1;inlined_47_i >= inlined_47_slots;inlined_47_i--)
                        {
                           inlined_6_q_m_data[inlined_47_r_m_base + inlined_47_i] = inlined_45_ref_m_data[inlined_47_a_m_base + inlined_47_i - inlined_47_slots];
                        }
                        for (inlined_47_i = inlined_47_slots - 1;inlined_47_i >= 0;inlined_47_i--)
                           inlined_6_q_m_data[inlined_47_r_m_base + inlined_47_i] = 0;
                        inlined_47_sv -= inlined_47_limbsShifted * 32;
                     }
                     else
                        if (inlined_6_q_m_data != inlined_45_ref_m_data)
                        {
                           {
                              int inlined_48_orig_m_size = inlined_47_a_m_size;
                              unsigned int inlined_48_orig_m_base = inlined_47_a_m_base;
                              int inlined_48_m_size = inlined_47_r_m_size;
                              unsigned int inlined_48_m_base = inlined_47_r_m_base;
                              int inlined_48_minCopySize = (inlined_48_m_size < inlined_48_orig_m_size)? inlined_48_m_size : inlined_48_orig_m_size;
                              int inlined_48_i;
                              for (inlined_48_i = 0;inlined_48_i < inlined_48_minCopySize;inlined_48_i++)
                                 inlined_6_q_m_data[inlined_48_m_base + inlined_48_i] = inlined_45_ref_m_data[inlined_48_orig_m_base + inlined_48_i];
                              for ( ;inlined_48_i < inlined_48_m_size;inlined_48_i++)
                                 inlined_6_q_m_data[inlined_48_m_base + inlined_48_i] = 0;
                           }
                        }
                     int inlined_47_cs = 32 - inlined_47_sv;
                     for (inlined_47_i = 0;inlined_47_i < inlined_47_r_m_size;inlined_47_i++)
                     {
                        int inlined_47_nv = (inlined_6_q_m_data[inlined_47_r_m_base + inlined_47_i] << inlined_47_sv) | inlined_47_carry;
                        inlined_47_carry = ~((0xFFFFFFFF << inlined_47_sv) >> inlined_47_sv);
                        inlined_47_carry &= inlined_6_q_m_data[inlined_47_r_m_base + inlined_47_i];
                        inlined_47_carry = inlined_47_carry >> inlined_47_cs;
                        inlined_6_q_m_data[inlined_47_r_m_base + inlined_47_i] = inlined_47_nv;
                     }
                  }
               }
            }
            else
{
               {
                  int inlined_49_bits = 1;
                  int inlined_49_m_size = inlined_6_q_m_size;
                  unsigned int inlined_49_m_base = inlined_6_q_m_base;
                  unsigned int inlined_49_ref_m_data[inlined_49_m_size];
                  unsigned int inlined_49_ref_m_base = inlined_49_m_base;
                  int inlined_49_ref_m_size = inlined_49_m_size;
                  {
                     int inlined_50_orig_m_size = inlined_49_m_size;
                     unsigned int inlined_50_orig_m_base = inlined_49_m_base;
                     int inlined_50_m_size = inlined_49_ref_m_size;
                     unsigned int inlined_50_m_base = inlined_49_ref_m_base;
                     int inlined_50_minCopySize = (inlined_50_m_size < inlined_50_orig_m_size)? inlined_50_m_size : inlined_50_orig_m_size;
                     int inlined_50_i;
                     for (inlined_50_i = 0;inlined_50_i < inlined_50_minCopySize;inlined_50_i++)
                        inlined_49_ref_m_data[inlined_50_m_base + inlined_50_i] = inlined_6_q_m_data[inlined_50_orig_m_base + inlined_50_i];
                     for ( ;inlined_50_i < inlined_50_m_size;inlined_50_i++)
                        inlined_49_ref_m_data[inlined_50_m_base + inlined_50_i] = 0;
                  }
                  {
                     int inlined_51_sv = inlined_49_bits;
                     int inlined_51_a_m_size = inlined_49_ref_m_size;
                     unsigned int inlined_51_a_m_base = inlined_49_ref_m_base;
                     int inlined_51_r_m_size = inlined_49_m_size;
                     unsigned int inlined_51_r_m_base = inlined_49_m_base;
                     int inlined_51_i;
                     unsigned int inlined_51_carry = 0;
                     int inlined_51_limbsShifted = inlined_51_sv / 32;
                     if (inlined_51_limbsShifted)
                     {
                        int inlined_51_slots = inlined_51_limbsShifted;
                        for (inlined_51_i = inlined_51_r_m_size - 1;inlined_51_i >= inlined_51_slots;inlined_51_i--)
                        {
                           inlined_6_q_m_data[inlined_51_r_m_base + inlined_51_i] = inlined_49_ref_m_data[inlined_51_a_m_base + inlined_51_i - inlined_51_slots];
                        }
                        for (inlined_51_i = inlined_51_slots - 1;inlined_51_i >= 0;inlined_51_i--)
                           inlined_6_q_m_data[inlined_51_r_m_base + inlined_51_i] = 0;
                        inlined_51_sv -= inlined_51_limbsShifted * 32;
                     }
                     else
                        if (inlined_6_q_m_data != inlined_49_ref_m_data)
                        {
                           {
                              int inlined_52_orig_m_size = inlined_51_a_m_size;
                              unsigned int inlined_52_orig_m_base = inlined_51_a_m_base;
                              int inlined_52_m_size = inlined_51_r_m_size;
                              unsigned int inlined_52_m_base = inlined_51_r_m_base;
                              int inlined_52_minCopySize = (inlined_52_m_size < inlined_52_orig_m_size)? inlined_52_m_size : inlined_52_orig_m_size;
                              int inlined_52_i;
                              for (inlined_52_i = 0;inlined_52_i < inlined_52_minCopySize;inlined_52_i++)
                                 inlined_6_q_m_data[inlined_52_m_base + inlined_52_i] = inlined_49_ref_m_data[inlined_52_orig_m_base + inlined_52_i];
                              for ( ;inlined_52_i < inlined_52_m_size;inlined_52_i++)
                                 inlined_6_q_m_data[inlined_52_m_base + inlined_52_i] = 0;
                           }
                        }
                     int inlined_51_cs = 32 - inlined_51_sv;
                     for (inlined_51_i = 0;inlined_51_i < inlined_51_r_m_size;inlined_51_i++)
                     {
                        int inlined_51_nv = (inlined_6_q_m_data[inlined_51_r_m_base + inlined_51_i] << inlined_51_sv) | inlined_51_carry;
                        inlined_51_carry = ~((0xFFFFFFFF << inlined_51_sv) >> inlined_51_sv);
                        inlined_51_carry &= inlined_6_q_m_data[inlined_51_r_m_base + inlined_51_i];
                        inlined_51_carry = inlined_51_carry >> inlined_51_cs;
                        inlined_6_q_m_data[inlined_51_r_m_base + inlined_51_i] = inlined_51_nv;
                     }
                  }
               }
               {
                  int inlined_53_m_size = inlined_6_q_m_size;
                  unsigned int inlined_53_m_base = inlined_6_q_m_base;
                  int inlined_53_i = 0;
                  int inlined_53_doRun = 1;
                  while (inlined_53_doRun)
                  {
                     inlined_6_q_m_data[inlined_53_m_base + inlined_53_i]++;
                     inlined_53_doRun = (inlined_6_q_m_data[inlined_53_m_base + inlined_53_i] == 0);
                     inlined_53_i++;
                  }
               }
               {
                  int inlined_54_y_m_size = inlined_6_divisor_m_size;
                  unsigned int inlined_54_y_m_base = inlined_6_divisor_m_base;
                  int inlined_54_m_size = inlined_6_r_m_size;
                  unsigned int inlined_54_m_base = inlined_6_r_m_base;
                  unsigned int inlined_54_ref_m_data[inlined_54_m_size];
                  unsigned int inlined_54_ref_m_base = 0;
                  int inlined_54_ref_m_size = inlined_54_m_size;
                  {
                     int inlined_55_orig_m_size = inlined_54_m_size;
                     unsigned int inlined_55_orig_m_base = inlined_54_m_base;
                     int inlined_55_m_size = inlined_54_ref_m_size;
                     unsigned int inlined_55_m_base = inlined_54_ref_m_base;
                     int inlined_55_minCopySize = (inlined_55_m_size < inlined_55_orig_m_size)? inlined_55_m_size : inlined_55_orig_m_size;
                     int inlined_55_i;
                     for (inlined_55_i = 0;inlined_55_i < inlined_55_minCopySize;inlined_55_i++)
                        inlined_54_ref_m_data[inlined_55_m_base + inlined_55_i] = inlined_6_r_m_data[inlined_55_orig_m_base + inlined_55_i];
                     for ( ;inlined_55_i < inlined_55_m_size;inlined_55_i++)
                        inlined_54_ref_m_data[inlined_55_m_base + inlined_55_i] = 0;
                  }
                  {
                     int inlined_56_y_m_size = inlined_54_y_m_size;
                     unsigned int inlined_56_y_m_base = inlined_54_y_m_base;
                     int inlined_56_x_m_size = inlined_54_ref_m_size;
                     unsigned int inlined_56_x_m_base = inlined_54_ref_m_base;
                     int inlined_56_r_m_size = inlined_54_m_size;
                     unsigned int inlined_56_r_m_base = inlined_54_m_base;
                     unsigned int inlined_56_carryIn = 0;
                     unsigned int inlined_56_carryOut = 0;
                     for (int inlined_56_i = 0;inlined_56_i < inlined_56_x_m_size;inlined_56_i++)
                     {
                        unsigned int inlined_56_sum = 0;
                        if (inlined_56_i < inlined_56_x_m_size)
                           inlined_56_sum += inlined_54_ref_m_data[inlined_56_x_m_base + inlined_56_i];
                        if (inlined_56_i < inlined_56_y_m_size)
                           inlined_56_sum -= inlined_6_divisor_m_data[inlined_56_y_m_base + inlined_56_i];
                        if (inlined_56_sum > inlined_54_ref_m_data[inlined_56_x_m_base + inlined_56_i])
                        {
                           if (inlined_56_carryIn)
                              inlined_56_sum--;
                           inlined_56_carryOut = 1;
                        }
                        else
                           if (inlined_56_carryIn)
                           {
                              inlined_56_carryOut = (inlined_56_sum == 0)? 1 : 0;
                              inlined_56_sum--;
                           }
                        inlined_56_carryIn = inlined_56_carryOut;
                        inlined_6_r_m_data[inlined_56_r_m_base + inlined_56_i] = inlined_56_sum;
                     }
                  }
               }
            }
         }
         {
            int inlined_57_orig_m_size = inlined_6_r_m_size;
            unsigned int inlined_57_orig_m_base = inlined_6_r_m_base;
            int inlined_57_m_size = inlined_6_nr_m_size;
            unsigned int inlined_57_m_base = inlined_6_nr_m_base;
            int inlined_57_minCopySize = (inlined_57_m_size < inlined_57_orig_m_size)? inlined_57_m_size : inlined_57_orig_m_size;
            int inlined_57_i;
            for (inlined_57_i = 0;inlined_57_i < inlined_57_minCopySize;inlined_57_i++)
               nr[inlined_57_m_base + inlined_57_i] = inlined_6_r_m_data[inlined_57_orig_m_base + inlined_57_i];
            for ( ;inlined_57_i < inlined_57_m_size;inlined_57_i++)
               nr[inlined_57_m_base + inlined_57_i] = 0;
         }
         {
            int inlined_58_orig_m_size = inlined_6_q_m_size;
            unsigned int inlined_58_orig_m_base = inlined_6_q_m_base;
            int inlined_58_m_size = inlined_6_nq_m_size;
            unsigned int inlined_58_m_base = inlined_6_nq_m_base;
            int inlined_58_minCopySize = (inlined_58_m_size < inlined_58_orig_m_size)? inlined_58_m_size : inlined_58_orig_m_size;
            int inlined_58_i;
            for (inlined_58_i = 0;inlined_58_i < inlined_58_minCopySize;inlined_58_i++)
               nq[inlined_58_m_base + inlined_58_i] = inlined_6_q_m_data[inlined_58_orig_m_base + inlined_58_i];
            for ( ;inlined_58_i < inlined_58_m_size;inlined_58_i++)
               nq[inlined_58_m_base + inlined_58_i] = 0;
         }
      }
   }
}
